package com.ute.ticket.event.infrastructure.cache;

import com.ute.ticket.event.application.port.out.CachePreSale;
import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisSessionCachePresale implements CachePreSale {

    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> initInventoryScript;

    @Override
    public void warmUp(
            List<Session> sessions,
            Map<Long, List<TicketType>> ticketTypesBySession,
            Map<Long, Inventory> inventoryByTicketType) {
        try {
            redisTemplate.executePipelined((RedisCallback<?>) connection -> {
                StringRedisConnection conn = (StringRedisConnection) connection;
                for (Session s : sessions) {
                    long ttlSeconds = Duration.between(Instant.now(), s.getSalesEndAt().plus(Duration.ofHours(1))).getSeconds();
                    String sessionKey = "session:" + s.getId();
                    Map<String, String> sessionFields = new HashMap<>();
                    sessionFields.put("id", String.valueOf(s.getId()));
                    sessionFields.put("eventId", String.valueOf(s.getEventId()));
                    sessionFields.put("startTime", s.getStartTime().toString());
                    sessionFields.put("endTime", s.getEndTime().toString());
                    sessionFields.put("salesStartAt", s.getSalesStartAt().toString());
                    sessionFields.put("salesEndAt", s.getSalesEndAt().toString());
                    sessionFields.put("salesStartAtEpoch", String.valueOf(s.getSalesStartAt().getEpochSecond()));
                    sessionFields.put("salesEndAtEpoch", String.valueOf(s.getSalesEndAt().getEpochSecond()));
                    sessionFields.put("status", s.getStatus().name());
                    conn.hMSet(sessionKey, sessionFields);
                    conn.expire(sessionKey, ttlSeconds);
                    for (TicketType tt : ticketTypesBySession.getOrDefault(s.getId(), List.of())) {
                        String ttKey = "tickettype:" + tt.getId();
                        Map<String, String> ttFields = new HashMap<>();
                        ttFields.put("id", String.valueOf(tt.getId()));
                        ttFields.put("sessionId", String.valueOf(tt.getSessionId()));
                        ttFields.put("name", tt.getName());
                        ttFields.put("price", tt.getPrice().toPlainString());
                        ttFields.put("maxPerUser", String.valueOf(tt.getMaxPerUser()));
                        ttFields.put("status", tt.getStatus().name());
                        conn.hMSet(ttKey, ttFields);
                        conn.expire(ttKey, ttlSeconds);
                        conn.sAdd(sessionKey + ":tickettypes", String.valueOf(tt.getId()));
                        conn.expire(sessionKey + ":tickettypes", ttlSeconds);
                    }
                }
                return null;
            });

            for (Session s : sessions) {
                long ttlSeconds = Duration.between(Instant.now(), s.getSalesEndAt().plus(Duration.ofHours(1))).getSeconds();
                for (TicketType tt : ticketTypesBySession.getOrDefault(s.getId(), List.of())) {
                    Inventory inv = inventoryByTicketType.get(tt.getId());
                    if (inv == null) continue;
                    String invKey = "inventory:" + tt.getId();
                    String availKey = "inventory:available:" + tt.getId();
                    redisTemplate.execute(
                            initInventoryScript,
                            List.of(invKey, availKey),
                            String.valueOf(inv.getTotalStock()),
                            String.valueOf(inv.getReservedStock()),
                            String.valueOf(inv.getSoldStock()),
                            inv.getStatus().name(),
                            String.valueOf(ttlSeconds)
                    );
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
