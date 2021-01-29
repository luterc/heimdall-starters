package com.luter.heimdall.starter.jpa.helper;

import cn.hutool.json.JSONUtil;
import com.luter.heimdall.starter.model.pagination.PageDTO;
import com.luter.heimdall.starter.model.pagination.PagerVO;
import com.luter.heimdall.starter.model.pagination.SorterVO;
import com.luter.heimdall.starter.utils.BeanPlusUtil;
import com.luter.heimdall.starter.utils.sql.AntiSQLFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JpaPaginationHelper {

    private final SpringDataWebProperties properties;

    public Pageable getPager(PagerVO pager) {
        final int pageNo = pager.getPage();
        final int pageSize = pager.getSize();
        final List<SorterVO> orders = pager.getOrders();
        //页码小于1默认给1
        int page = pageNo > 0 ? pageNo : 1;
        //size超过最大，给默认
        int size = Math.min(pageSize, properties.getPageable().getMaxPageSize());
        //页码是否从0开始?
        page = properties.getPageable().isOneIndexedParameters() ? page - 1 : page;
        log.debug("分页参数===page:{},size:{},\n分页配置:{}", page, size, JSONUtil.toJsonPrettyStr(properties.getPageable()));
        if (null != orders && !orders.isEmpty()) {
            final List<Sort.Order> collect = orders.stream().map((d)
                    -> d.isAsc() ? Sort.Order.asc(AntiSQLFilter.getSafeValue(d.getProperty()))
                    : Sort.Order.desc(AntiSQLFilter.getSafeValue(d.getProperty())))
                    .collect(Collectors.toList());
            return PageRequest.of(page, size, Sort.by(collect));
        }
        return PageRequest.of(page, size);
    }

    public Sort getSorter(PagerVO pager) {
        final List<SorterVO> orders = pager.getOrders();
        if (null != orders && !orders.isEmpty()) {
            final List<Sort.Order> collect = orders.stream().map((d)
                    -> d.isAsc() ? Sort.Order.asc(AntiSQLFilter.getSafeValue(d.getProperty()))
                    : Sort.Order.desc(AntiSQLFilter.getSafeValue(d.getProperty())))
                    .collect(Collectors.toList());
            return Sort.by(collect);
        }
        return Sort.unsorted();
    }

    public <D, T> PageDTO<D> toPageData(Page<T> entities, Class<D> dtoClass) {
        final Page<D> map = entities.map((t) -> {
            try {
                D d = dtoClass.newInstance();
                BeanPlusUtil.copyProperties(t, d);
                return d;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }

        });
        return toPageData(map);

    }

    public <T> PageDTO<T> toPageData(Page<T> page) {
        if (null != page) {
            PageDTO<T> pageDataDTO = new PageDTO<>();
            pageDataDTO.setPageNumber(page.getNumber() + 1);
            pageDataDTO.setPageSize(page.getSize());
            pageDataDTO.setRecords(page.getContent());
            pageDataDTO.setTotalCount(page.getTotalElements());
            pageDataDTO.setPageCount(page.getTotalPages());
            pageDataDTO.setEmpty(page.isEmpty());
            pageDataDTO.setFirst(page.isFirst());
            pageDataDTO.setLast(page.isLast());
            pageDataDTO.setRecordCount(page.getNumberOfElements());
            return pageDataDTO;
        }

        return null;

    }


    @Deprecated
    private Sort parseSortQuery(final String[] query, String delimiter) {
        final List<Sort.Order> orders = new ArrayList<>();
        for (String q : query) {
            if (q == null) {
                continue;
            }
            final String[] parts = q.split(delimiter);
            Sort.Direction direction = null;
            if (parts.length > 0) {
                final Optional<Sort.Direction> d = Sort.Direction.fromOptionalString(parts[parts.length - 1]);
                if (d.isPresent()) {
                    direction = d.get();
                }
            }
            for (int i = 0; i < parts.length; i++) {
                if (i == parts.length - 1) {
                    continue;
                }
                final String property = parts[i];
                if (!StringUtils.hasText(property)) {
                    continue;
                }
                orders.add(new Sort.Order(direction, property));
            }
        }
        return Sort.by(orders);
    }
}
