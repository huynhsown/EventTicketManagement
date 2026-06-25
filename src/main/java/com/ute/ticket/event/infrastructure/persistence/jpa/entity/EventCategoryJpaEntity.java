package com.ute.ticket.event.infrastructure.persistence.jpa.entity;

import com.ute.ticket.shared.config.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_categories")
@IdClass(EventCategoryJpaEntity.EventCategoryId.class)
public class EventCategoryJpaEntity extends BaseEntity {

    @Id
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Id
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class EventCategoryId implements Serializable {
        private Long eventId;
        private Long categoryId;
    }
}
