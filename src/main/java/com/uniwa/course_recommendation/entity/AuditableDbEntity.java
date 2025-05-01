package com.uniwa.course_recommendation.entity;

import java.time.LocalDateTime;

import com.uniwa.course_recommendation.entity.DbEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableDbEntity extends DbEntity {

    @Column(name = "inserted_at")
    private LocalDateTime insertedAt;

    @Column(name = "inserted_by")
    private String insertedBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Version
    @Column(name = "row_version")
    private Long rowVersion;
//TODO: CHANGE inserted by and modified by default value
    @PrePersist
    protected void onCreate() {
        this.insertedAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
        this.insertedBy = "system";
        this.modifiedBy = "system";
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
        this.modifiedBy = "system";
    }
}
