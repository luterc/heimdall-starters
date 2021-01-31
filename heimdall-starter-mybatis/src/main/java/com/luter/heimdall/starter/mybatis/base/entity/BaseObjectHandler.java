/*
 *
 *  *
 *  *
 *  *      Copyright 2020-2021 Luter.me
 *  *
 *  *      Licensed under the Apache License, Version 2.0 (the "License");
 *  *      you may not use this file except in compliance with the License.
 *  *      You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *      Unless required by applicable law or agreed to in writing, software
 *  *      distributed under the License is distributed on an "AS IS" BASIS,
 *  *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *      See the License for the specific language governing permissions and
 *  *      limitations under the License.
 *  *
 *  *
 *
 */

package com.luter.heimdall.starter.mybatis.base.entity;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.luter.heimdall.starter.utils.context.BaseContextHolder;
import com.luter.heimdall.starter.utils.json.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

@Slf4j
public class BaseObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.created(metaObject);
        this.updated(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.updated(metaObject);
    }


    private void created(MetaObject metaObject) {
        log.debug("Mybatis Plus auto insert fill ");
        //如果是继承自实体类父类
        if (metaObject.getOriginalObject() instanceof MybatisAbstractEntity) {
            log.debug("MybatisAbstractEntity auto insert fill ");
            log.debug("原始对象:{}", JacksonUtils.toPrettyJson(metaObject.getOriginalObject()));
            MybatisAbstractEntity entity = (MybatisAbstractEntity) metaObject.getOriginalObject();
            //如果实体类没有设置创建人ID，则从ThreadLocal获取
            if (entity.getCreatedBy() == null) {
                this.setFieldValByName("createdBy", BaseContextHolder.getUserId(null), metaObject);
            }
        }
        //创建时间
        this.setFieldValByName("createdTime", LocalDateTime.now(), metaObject);
    }

    private void updated(MetaObject metaObject) {
        log.debug("Mybatis Plus auto update fill ");

        //如果是继承自实体类父类
        if (metaObject.getOriginalObject() instanceof MybatisAbstractEntity) {
            log.debug("MybatisAbstractEntity auto update fill ");
            log.debug("原始对象:{}", JacksonUtils.toPrettyJson(metaObject.getOriginalObject()));
            MybatisAbstractEntity entity = (MybatisAbstractEntity) metaObject.getOriginalObject();
            //如果实体类上传入的参数里，没有修改人ID，则从ThreadLocal获取
            if (entity.getLastModifiedBy() == null) {
                this.setFieldValByName("lastModifiedBy", BaseContextHolder.getUserId(null), metaObject);
            }
        }
        //更新最后修改时间
        this.setFieldValByName("lastModifiedTime", LocalDateTime.now(), metaObject);
    }
}
