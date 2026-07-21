package com.ute.ticket.event.infrastructure.cache;

import com.ute.ticket.event.application.port.out.EventCachePort;
import com.ute.ticket.event.application.result.EventDetailResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisEventCacheRepository implements EventCachePort {

    private static final String KEY_PREFIX = "event:detail:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public EventDetailResult findBySlug(String slug) {
        return (EventDetailResult) redisTemplate.opsForValue()
                .get(KEY_PREFIX + slug);
    }

    @Override
    public void save(EventDetailResult result) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + result.slug(),
                result,
                TTL
        );
    }

    @Override
    public void evict(String slug) {
        redisTemplate.delete(KEY_PREFIX + slug);
    }
}
