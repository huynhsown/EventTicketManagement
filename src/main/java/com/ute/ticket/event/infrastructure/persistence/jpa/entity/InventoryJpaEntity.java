package com.ute.ticket.event.infrastructure.persistence.jpa.entity;

import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.shared.config.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventories")
public class InventoryJpaEntity extends BaseEntity {

    @Id
    @Column(name = "ticket_type_id", nullable = false)
    private Long ticketTypeId;

    @Column(name = "total_stock", nullable = false)
    private Integer totalStock;

    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock;

    @Column(name = "sold_stock", nullable = false)
    private Integer soldStock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private InventoryStatus status;
}
