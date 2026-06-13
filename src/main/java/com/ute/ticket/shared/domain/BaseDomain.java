package com.ute.ticket.shared.domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.Instant;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseDomain {

    protected Instant createdAt;
    protected Instant updatedAt;
    protected String createdBy;
    protected String updatedBy;
    protected Instant deletedAt;
    protected Long version = 0L;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void markDeleted() {
        this.deletedAt = Instant.now();
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    protected void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }
}
