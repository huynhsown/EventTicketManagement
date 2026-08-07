package com.ute.ticket.event.infrastructure.cache;

import com.ute.ticket.event.application.port.out.InventoryReservationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisInventoryReservationAdapter implements InventoryReservationPort {

    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> decreaseInventoryScript;
    private final RedisScript<Long> releaseInventoryScript;

    @Override
    public long decrease(Long sessionId, Long ticketTypeId, long quantity, long nowEpochSeconds) {
        return redisTemplate.execute(
                decreaseInventoryScript,
                List.of(
                        "session:" + sessionId,
                        "inventory:" + ticketTypeId,
                        "inventory:available:" + ticketTypeId
                ),
                String.valueOf(quantity),
                String.valueOf(nowEpochSeconds)
        );
    }

    @Override
    public long release(Long ticketTypeId, int quantity) {
        String inventoryKey = "inventory:" + ticketTypeId;
        String availableKey = "inventory:available:" + ticketTypeId;
        return redisTemplate.execute(
                releaseInventoryScript,
                List.of(inventoryKey, availableKey),
                String.valueOf(quantity)
        );
    }
}