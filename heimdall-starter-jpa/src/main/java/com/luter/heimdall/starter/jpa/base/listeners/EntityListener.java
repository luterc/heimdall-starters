/*
 *
 *  *
 *  *  *    Copyright 2020-2021 Luter.me
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *
 */

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
