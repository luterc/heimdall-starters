package com.luter.heimdall.starter.jpa.base.listeners;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import javax.persistence.*;

@Slf4j
public class EntityListener {
    @PostLoad
    public void postLoad(Object entity) {
        log.warn("加载之后:{}", entity.toString());
    }

    @PrePersist
    public void prePersist(Object entity) {
        log.warn("保存之前:{}", entity.toString());
    }

    @PostPersist
    public void postPersist(Object entity) {
        log.warn("保存之后:{}", entity.toString());
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        log.warn("更新之前:{}", entity.toString());
    }

    @PostUpdate
    public void postUpdate(Object entity) {
        log.warn("更新之后:{}", entity.toString());
    }

    @PreRemove
    public void preRemove(Object entity) {
        log.warn("删除之前:{}", entity.toString());
    }

    @PostRemove
    public void postRemovet(Object entity) {
        log.warn("删除之后:{}", entity.toString());
    }

    @PreDestroy
    public void preDestroy(Object entity) {
        log.warn("销毁之前:{}", entity.toString());
    }
}
