
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

package com.luter.heimdall.starter.mybatis.base.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luter.heimdall.starter.model.pagination.PageDTO;
import com.luter.heimdall.starter.model.pagination.PagerVO;
import com.luter.heimdall.starter.mybatis.base.entity.MybatisAbstractEntity;
import com.luter.heimdall.starter.mybatis.base.service.BaseMybatisService;
import com.luter.heimdall.starter.mybatis.helper.MybatisPaginationHelper;
import com.luter.heimdall.starter.utils.exception.LuterIdEntityNotFoundException;
import com.luter.heimdall.starter.utils.exception.LuterIllegalParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Slf4j
public abstract class BaseMybatisServiceImpl<M extends BaseMapper<T>, T extends MybatisAbstractEntity> extends ServiceImpl<M, T> implements BaseMybatisService<T> {


    @Autowired
    public MybatisPaginationHelper mybatisQueryHelper;

    @Override
    public T getById(Serializable id, boolean throwEx) {
        if (null == id) {
            throw new LuterIllegalParameterException("参数错误:ID");
        }
        final T byId = getById(id);
        if (null == byId && throwEx) {
            throw new LuterIdEntityNotFoundException("数据未找到,ID:" + id.toString());
        }
        return byId;
    }

    @Override
    public T get(Serializable id) {
        return getById(id, true);
    }

    @Override
    public boolean updateEntity(T entity) {
        return updateById(entity);
    }

    @Override
    public boolean exist(String prop, Object value) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq(prop, value);
        final int count = count(wrapper);
        return count > 0;
    }

    @Override
    public boolean saveIdempotency(T entity, String lockKey, Wrapper<T> countWrapper, String msg) {

        return false;
    }

    @Override
    public boolean saveOrUpdateIdempotency(T entity, String lockKey, Wrapper<T> countWrapper, String msg) {
        return false;
    }

    @Override
    public boolean saveIdempotency(T entity, String lockKey, Wrapper<T> countWrapper) {
        return saveIdempotency(entity, lockKey, countWrapper, null);
    }


    @Override
    public boolean saveOrUpdateIdempotency(T entity, String lockKey, Wrapper<T> countWrapper) {
        return this.saveOrUpdateIdempotency(entity, lockKey, countWrapper, null);
    }

    public <T extends MybatisAbstractEntity> Page<T> getPager(PagerVO pagerVO) {
        return mybatisQueryHelper.getPager(pagerVO);
    }

    public <S, E> PageDTO<S> toPageData(Page<E> page, Class<S> sClass, String... ignoreProperties) {
        return mybatisQueryHelper.toPageData(page, sClass, ignoreProperties);
    }
}

