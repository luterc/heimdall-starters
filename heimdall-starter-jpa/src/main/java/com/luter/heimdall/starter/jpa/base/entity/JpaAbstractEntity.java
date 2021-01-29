package com.luter.heimdall.starter.jpa.base.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Version;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@JsonIgnoreProperties({"lastModifiedTime", "password", "salt", "version", "createdTime", "createdBy", "lastModifiedBy"})
public abstract class JpaAbstractEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "SnowFlakeGenerator")
    @GenericGenerator(name = "SnowFlakeGenerator",
            strategy = "com.luter.heimdall.starter.jpa.base.entity.idgenerator.SnowflakeIdGenerator")
    @Column(name = "id")
    private Long id;
    @Column(name = "created_time")
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @Column(name = "created_by")
    @CreatedBy
    @JsonIgnore
    private Long createdBy;
    @Column(name = "last_modified_time")
    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedTime;
    @Column(name = "last_modified_by")
    @LastModifiedBy
    @JsonIgnore
    private Long lastModifiedBy;

    @Version
    @JsonIgnore
    private Integer version;

    private String remarks;

    @PrePersist
    public void prePersist() {
    }

    @PostPersist
    public void postPersist() {

    }

    @PreUpdate
    public void preUpdate() {
    }

    @PostUpdate
    public void postUpdate() {

    }

    @PreRemove
    public void preRemove() {
    }

    @PostRemove
    public void postRemove() {

    }

    @PostLoad
    public void postLoad() {

    }

}
