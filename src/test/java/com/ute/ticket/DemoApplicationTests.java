package com.ute.ticket;

import com.ute.ticket.order.domain.entity.Order;
import com.ute.ticket.order.domain.enums.OrderStatus;
import com.ute.ticket.order.infrastructure.persistence.jpa.entity.OrderJpaEntity;
import com.ute.ticket.order.infrastructure.persistence.jpa.mapper.OrderMapper;
import com.ute.ticket.order.infrastructure.persistence.jpa.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class DemoApplicationTests {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public DemoApplicationTests(
            OrderJpaRepository orderJpaRepository,
            OrderMapper orderMapper
    ) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMapper = orderMapper;
    }

    @Test
    @Transactional
    @Commit
    void contextLoads() {

        OrderJpaEntity orderJpaEntity =
                orderJpaRepository.findById(1L).orElseThrow();

        Order order = orderMapper.toDomain(orderJpaEntity);
//        order.changeStatus(OrderStatus.FAILED);
        orderMapper.updateOnlyOrderJpa(orderJpaEntity, order);
        orderJpaRepository.save(orderJpaEntity);

        System.out.println(order);
    }
}