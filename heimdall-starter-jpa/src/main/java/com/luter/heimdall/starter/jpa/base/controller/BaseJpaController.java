
package com.luter.heimdall.starter.jpa.base.controller;


import cn.hutool.core.date.DateUtil;
import com.luter.heimdall.starter.jpa.helper.JpaPaginationHelper;
import com.luter.heimdall.starter.model.pagination.PagerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.util.Date;

@Slf4j
public abstract class BaseJpaController {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    private JpaPaginationHelper jpaQueryHelper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtil.parseDateTime(text));
            }
        });
    }


    protected Pageable getPager(PagerVO pager) {
        return jpaQueryHelper.getPager(pager);
    }

}
