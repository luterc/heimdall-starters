
package com.luter.heimdall.starter.jpa.exception;


import com.luter.heimdall.starter.boot.exception.AbstractGlobalExceptionAdvise;
import com.luter.heimdall.starter.model.base.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public abstract class AbstractJpaGlobalExceptionAdvise extends AbstractGlobalExceptionAdvise {
    @ExceptionHandler(value = {DuplicateKeyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object duplicateKeyException(HttpServletRequest request, DuplicateKeyException e) {
        String error = "数据重复:" + (null != e.getCause() ? e.getCause().getMessage() : "");
        String msg = "数据重复,请检查数据是否正确?";
        log.error(LOG_FORMAT, e.getClass().getName(), msg, error, HttpStatus.BAD_REQUEST);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, msg, error));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object dataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
        String emsg = "保存数据出现错误:数据完整性错误,数据已经存在,超长，重复或者为空,请检查.";
        String error = HttpStatus.BAD_REQUEST.getReasonPhrase();
        Throwable cause = getRootExceptionThrowable(e);
        if (null != cause) {
            error = cause.getMessage();
        }
        log.error(LOG_FORMAT, e.getClass().getName(), emsg, error, HttpStatus.BAD_REQUEST);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, emsg, error));
    }


    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleConstaintViolatoinException(HttpServletRequest request, final TransactionSystemException e) {
        final Throwable rootExceptionThrowable = getRootExceptionThrowable(e);
        String error = rootExceptionThrowable.getMessage(), message = rootExceptionThrowable.getMessage();
        log.error(LOG_FORMAT, e.getClass().getName(), message, error, HttpStatus.BAD_REQUEST);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, message, error));
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object myRuntimeException(HttpServletRequest request, RuntimeException e) {
        e.printStackTrace();
        final Throwable rootExceptionThrowable = getRootExceptionThrowable(e);
        String msg = "服务错误:" + rootExceptionThrowable.getMessage(), error = rootExceptionThrowable.getMessage();
        if (e instanceof BadSqlGrammarException) {
            msg = "SQL参数错误";
            error = rootExceptionThrowable.getMessage();
        }
        log.error(LOG_FORMAT, rootExceptionThrowable.getClass().getName(), msg, error, HttpStatus.INTERNAL_SERVER_ERROR);
        return dealError(request, ResponseVO.fail(HttpStatus.INTERNAL_SERVER_ERROR, msg, error));
    }


}