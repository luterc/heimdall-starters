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

package com.luter.heimdall.starter.mybatis.helper;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luter.heimdall.starter.model.pagination.PageDTO;
import com.luter.heimdall.starter.model.pagination.PagerVO;
import com.luter.heimdall.starter.model.pagination.SorterVO;
import com.luter.heimdall.starter.mybatis.base.entity.MybatisAbstractEntity;
import com.luter.heimdall.starter.utils.BeanPlusUtil;
import com.luter.heimdall.starter.utils.sql.AntiSQLFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MybatisPaginationHelper {
    public static final int DEFAULT_PAGE_NO = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int MAX_PAGE_SIZE = 1000;

    public <T extends MybatisAbstractEntity> Page<T> getPager(PagerVO pagerVO) {
        return getPager(pagerVO, true, true);
    }

    public <T extends MybatisAbstractEntity> Page<T> getPager(PagerVO pagerVO, boolean isSorted) {
        return getPager(pagerVO, true, isSorted);
    }

    private <T extends MybatisAbstractEntity> Page<T> getPager(PagerVO pagerVO, boolean isCounted, boolean isSorted) {
        final int pageSize = pagerVO.getSize();
        final int pageNo = pagerVO.getPage();
        final List<SorterVO> orders = pagerVO.getOrders();
        Page<T> page = new Page<>(pageNo <= 0 ? DEFAULT_PAGE_NO : pageNo,
                pageSize > MAX_PAGE_SIZE ? DEFAULT_PAGE_SIZE : pageSize,
                isCounted);
        if (isSorted && null != orders && !orders.isEmpty()) {
            final List<OrderItem> orderItems = orders.stream().map((d) -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setAsc(d.isAsc());
                orderItem.setColumn(AntiSQLFilter.getSafeValue(d.getProperty()));
                return orderItem;
            }).collect(Collectors.toList());
            page.addOrder(orderItems);
        }
        return page;
    }

    public <S, E> PageDTO<S> toPageData(Page<E> page, Class<S> sClass, String... ignoreProperties) {
        PageDTO<S> pageDataDTO = new PageDTO<>();
        pageDataDTO.setPageNumber((int) page.getCurrent());
        pageDataDTO.setPageSize((int) page.getSize());
        pageDataDTO.setRecords(BeanPlusUtil.copyList(page.getRecords(), sClass, ignoreProperties));
        pageDataDTO.setTotalCount(page.getTotal());
        pageDataDTO.setPageCount((int) page.getPages());
        pageDataDTO.setEmpty(pageDataDTO.getTotalCount() <= 0);
        pageDataDTO.setFirst(page.getCurrent() == 1);
        pageDataDTO.setLast((page.getCurrent() + 1) >= page.getTotal());
        pageDataDTO.setRecordCount(page.getRecords().size());
        return pageDataDTO;

    }


}
