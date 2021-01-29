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
