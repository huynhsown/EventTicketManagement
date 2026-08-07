package com.ute.ticket.event.infrastructure.cache;

import com.ute.ticket.event.application.port.out.TicketTypeCachePort;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisTicketTypeCacheRepository implements TicketTypeCachePort {

    private static final String KEY_PREFIX = "tickettype:";

    private final StringRedisTemplate redisTemplate;

    @Override
    public Optional<TicketType> findActiveById(Long id) {
        Map<Object, Object> fields = redisTemplate.opsForHash()
                .entries(KEY_PREFIX + id);

        if (fields.isEmpty()) {
            return Optional.empty();
        }

        TicketTypeStatus status = TicketTypeStatus.valueOf((String) fields.get("status"));
        if (status != TicketTypeStatus.ACTIVE) {
            return Optional.empty();
        }

        TicketType ticketType = TicketType.builder()
                .id(Long.valueOf((String) fields.get("id")))
                .sessionId(Long.valueOf((String) fields.get("sessionId")))
                .name((String) fields.get("name"))
                .price(new BigDecimal((String) fields.get("price")))
                .maxPerUser(Integer.valueOf((String) fields.get("maxPerUser")))
                .status(status)
                .build();

        return Optional.of(ticketType);
    }
}