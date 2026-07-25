# Bug: RedisTemplate ClassCastException / SerializationException

## Error

When reading a cached value from Redis into `EventDetailResult`, the app threw:

```
java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class com.ute.ticket.event.application.result.EventDetailResult
```

After fixing that, two follow-up serialization errors appeared:

```
org.springframework.data.redis.serializer.SerializationException: Could not read JSON:
Could not resolve subtype of [simple type, class java.lang.Object]:
missing type id property '@class'

org.springframework.data.redis.serializer.SerializationException: Could not write JSON:
Java 8 date/time type `java.time.Instant` not supported by default:
add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
(through reference chain: ...SessionDetail["startTime"])
```

## Root cause

The value serializer for the `RedisTemplate<String, Object>` was built like this:

```java
GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
```

That constructor uses the given `ObjectMapper` **as-is**. The injected Spring Boot
`ObjectMapper` does not have Jackson default typing enabled, so:

1. Serialization wrote plain JSON without any `@class` type hint.
2. On read, there was no way to know the target type, so Redis deserialized into a
   generic `LinkedHashMap`, and the cast to `EventDetailResult` failed.

The naive replacement `new GenericJackson2JsonRedisSerializer()` (no-arg) fixes typing
but creates its **own** `ObjectMapper` that does not have the JSR-310 module registered,
so `java.time.Instant` fields (used in `EventDetailResult`, `SessionDetail.startTime`, etc.)
cannot be serialized.

## Fix

Use a copy of the auto-configured `ObjectMapper` (which already has `jackson-datatype-jsr310`
registered by Spring Boot) and enable Jackson default typing on that copy before handing it
to the serializer:

```java
ObjectMapper cacheMapper = objectMapper.copy();
cacheMapper.setDefaultTyping(new ObjectMapper.DefaultTypeResolverBuilder(
        ObjectMapper.DefaultTyping.EVERYTHING, cacheMapper.getPolymorphicTypeValidator())
        .init(JsonTypeInfo.Id.CLASS, null)
        .inclusion(JsonTypeInfo.As.PROPERTY));

GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(cacheMapper);
```

The resulting JSON embeds a `@class` type property, so deserialization restores the real
`EventDetailResult` (and nested records) instead of a `LinkedHashMap`, while `Instant`
fields are still handled.

## Notes

- Stale values written by the old serializer (no `@class`) cannot be read by the new
  serializer. Clear them once:
  `docker exec ticket-redis redis-cli --scan --pattern 'event:detail:*' | xargs docker exec ticket-redis redis-cli del`
- The serializer is configured in `src/main/java/com/ute/ticket/shared/config/RedisConfig.java`.
- Used by `RedisEventCacheRepository` via `RedisTemplate<String, Object>`.
