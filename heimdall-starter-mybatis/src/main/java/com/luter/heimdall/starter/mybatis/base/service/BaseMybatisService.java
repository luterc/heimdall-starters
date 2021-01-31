

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

package com.luter.heimdall.starter.mybatis.base.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;


public interface BaseMybatisService<T> extends IService<T> {


    boolean exist(String prop, Object value);

    T getById(Serializable id, boolean throwEx);

    T get(Serializable id);

    boolean saveIdempotency(T entity, String lockKey, Wrapper<T> countWrapper, String msg);

    boolean saveIdempotency(T entity, String lockKey, Wrapper<T> countWrapper);

    boolean saveOrUpdateIdempotency(T entity, String lockKey, Wrapper<T> countWrapper, String msg);

    boolean saveOrUpdateIdempotency(T entity, String lockKey, Wrapper<T> countWrapper);

}
