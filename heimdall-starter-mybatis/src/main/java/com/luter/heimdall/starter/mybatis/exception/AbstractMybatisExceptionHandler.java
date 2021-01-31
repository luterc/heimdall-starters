
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

package com.luter.heimdall.starter.mybatis.exception;


import com.luter.heimdall.starter.boot.exception.AbstractGlobalExceptionAdvise;
import com.luter.heimdall.starter.model.base.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.ParseException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public abstract class AbstractMybatisExceptionHandler extends AbstractGlobalExceptionAdvise {
    @ExceptionHandler(value = {MyBatisSystemException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object myBatisSystemException(HttpServletRequest request, MyBatisSystemException e) {
        final Throwable rootExceptionThrowable = getRootExceptionThrowable(e);
        String msg = "数据处理错误:" + rootExceptionThrowable.getMessage(), error = rootExceptionThrowable.getMessage();
        if (rootExceptionThrowable instanceof ParseException) {
            msg = "查询参数解析错误,请检查参数是否正确?";
            error = msg;
        }
        log.error(LOG_FORMAT, rootExceptionThrowable.getClass().getName(), msg, error, HttpStatus.INTERNAL_SERVER_ERROR);
        return dealError(request, ResponseVO.fail(HttpStatus.INTERNAL_SERVER_ERROR, msg, error));
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object myRuntimeException(HttpServletRequest request, RuntimeException e) {
        final Throwable rootExceptionThrowable = getRootExceptionThrowable(e);
        String msg = "数据处理错误:" + rootExceptionThrowable.getMessage(), error = rootExceptionThrowable.getMessage();
        if (e instanceof BadSqlGrammarException) {
            msg = "SQL参数错误";
            error = rootExceptionThrowable.getMessage();
        }
        log.error(LOG_FORMAT, rootExceptionThrowable.getClass().getName(), msg, error, HttpStatus.INTERNAL_SERVER_ERROR);
        return dealError(request, ResponseVO.fail(HttpStatus.INTERNAL_SERVER_ERROR, msg, error));
    }
}