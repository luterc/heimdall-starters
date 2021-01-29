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

package com.luter.heimdall.starter.jpa.base.service.impl;

import com.luter.heimdall.starter.jpa.base.service.BaseJpaService;
import com.luter.heimdall.starter.jpa.helper.JpaPaginationHelper;
import com.luter.heimdall.starter.model.pagination.PageDTO;
import com.luter.heimdall.starter.model.pagination.PagerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
public abstract class BaseJpaServiceImpl extends HibernateServiceImpl implements BaseJpaService {


    @Autowired
    private JpaPaginationHelper jpaQueryHelper;


    protected Sort getSorter(PagerVO pager) {
        return jpaQueryHelper.getSorter(pager);
    }

    protected Pageable getPager(PagerVO pager) {
        return jpaQueryHelper.getPager(pager);
    }

    protected <T> PageDTO<T> toPageData(Page<T> page) {
        return jpaQueryHelper.toPageData(page);
    }

    protected <D, T> PageDTO<D> toPageData(Page<T> entities, Class<D> dtoClass) {
        return jpaQueryHelper.toPageData(entities, dtoClass);
    }
}
