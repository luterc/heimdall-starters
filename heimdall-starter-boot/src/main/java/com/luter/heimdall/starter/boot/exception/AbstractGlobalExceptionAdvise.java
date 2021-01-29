
/*
 *
 *  *
 *  *  *    Copyright 2020-2021 Luter.me
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *
 */

package com.luter.heimdall.starter.boot.exception;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ValidateException;
import com.luter.heimdall.starter.model.base.ResponseVO;
import com.luter.heimdall.starter.utils.exception.*;
import com.luter.heimdall.starter.utils.response.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class AbstractGlobalExceptionAdvise {
    public static final String LOG_FORMAT = "\n===发生错误:\nException:{}\nMessage:{}\nError:{}\nStatus:{}\n";
    @Value("${server.error.path:${error.path:error}}")
    public String errorView;


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Object noRequestHandlerFoundExceptionHandler(HttpServletRequest request, NoHandlerFoundException e) {
        String msg = "没找到您要访问的资源:" + request.getMethod() + ":" + request.getRequestURI(),
                error = e.getMessage();
        log.error(LOG_FORMAT, e.getClass().getName(), msg, error, HttpStatus.NOT_FOUND);
        return dealError(request, ResponseVO.fail(HttpStatus.NOT_FOUND, msg, error));
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Object multipartExceptionHandler(HttpServletRequest request, MultipartException e) {
        String eMsg = "文件上传错误";
        Throwable rootExceptionThrowable = getRootExceptionThrowable(e);
        log.error(LOG_FORMAT, e.getClass().getName(), eMsg, rootExceptionThrowable.getMessage(), HttpStatus.BAD_REQUEST);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, eMsg, rootExceptionThrowable.getMessage()));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException e) {
        String msg, error = e.getLocalizedMessage() + "." + (null != e.getCause() ? e.getCause().getMessage() : "");
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        msg = constraintViolations.iterator().next().getMessage();
        log.error(LOG_FORMAT, e.getClass().getName(), msg, error, HttpStatus.BAD_REQUEST);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, msg, error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object resolveMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        ResponseVO<Void> responseVo = ResponseVO.fail(HttpStatus.BAD_REQUEST, "请确认参数是否正确？", "Validate Faild");
        List<FieldError> objectErrors = ex.getBindingResult().getFieldErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            //拿所有错误消息
//            for (FieldError fieldError : objectErrors) {
//                msgBuilder.append("参数[").append(fieldError.getField()).append("]:").append(fieldError.getDefaultMessage()).append(";");
//            }
//            String errorMessage = msgBuilder.toString();
//            if (errorMessage.length() > 1) {
//                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
//            }
            //拿第一个就返回
            FieldError fieldError = objectErrors.get(0);
            msgBuilder.append("参数[").append(fieldError.getField()).append("]:").append(fieldError.getDefaultMessage());
            responseVo.setMsg(msgBuilder.toString());
        }
        log.error(LOG_FORMAT, ex.getClass().getName(), responseVo.getMsg(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        return dealError(request, responseVo);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public Object httpRequestMethodNotSupportedException(HttpServletRequest request,
                                                         HttpRequestMethodNotSupportedException e) {
        String error = e.getMessage(), msg = "访问方法不被支持:" + request.getMethod() + ":" + request.getRequestURI();
        log.error(LOG_FORMAT, e.getClass().getName(), msg, error, HttpStatus.METHOD_NOT_ALLOWED);
        return dealError(request, ResponseVO.fail(HttpStatus.METHOD_NOT_ALLOWED, msg, error));
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exception(HttpServletRequest request, Exception e) {
        String message = "系统异常";
        if (e instanceof IllegalArgumentException) {
            message = "参数错误,请检查参数是否正确";
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            message = "参数类型不匹配,请检查参数是否正确";
        } else if (e instanceof UnexpectedTypeException) {
            //验证规则配置错误，比如：将@Pattern、NotBlank用在非String字段上等等
            message = "参数验证规则错误";
        } else if (e instanceof MissingServletRequestPartException) {
            //文件上传的时候，文件字段参数，比如:file 写的不对或者没有
            message = "上传错误,请检查上传参数是否正确?";
        }
        Throwable resultCause = getRootExceptionThrowable(e);
        String error = resultCause.getMessage();
        if (resultCause instanceof SQLIntegrityConstraintViolationException) {
            message = "数据不存在或者出现重复,请检查参数是否正确";
        }
        log.error(LOG_FORMAT, e.getClass().getName(), message, error, HttpStatus.INTERNAL_SERVER_ERROR);
        return dealError(request, ResponseVO.fail(HttpStatus.INTERNAL_SERVER_ERROR, message, error));
    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object bindException(HttpServletRequest request, BindException e) {
        StringBuilder bfMsg = new StringBuilder();
        StringBuilder bfError = new StringBuilder();
        List<FieldError> fieldErrors = e.getFieldErrors();
        fieldErrors.forEach((oe) -> {
                    bfMsg.append("参数:[").append(oe.getObjectName())
                            .append(".").append(oe.getField())
                            .append("]的传入值:[").append(oe.getRejectedValue()).append("]与预期的类型不匹配.");
                    bfError.append(oe.getDefaultMessage());
                }
        );
        log.error(LOG_FORMAT, e.getClass().getName(), bfMsg.toString(), e.getMessage(), HttpStatus.BAD_REQUEST);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, bfMsg.toString(), bfError.toString()));
    }


    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        final Throwable rootExceptionThrowable = getRootExceptionThrowable(e);
        String error = rootExceptionThrowable.getMessage(),
                msg = "请求参数错误,请检查参数类型或者格式是否正确?";
        log.error(LOG_FORMAT, e.getClass().getName(), msg, error, HttpStatus.BAD_REQUEST);

        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, msg, error));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object missingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException ex) {
        String error = ex.getMessage(),
                msg = "缺少: [" + ex.getParameterType() + "] 类型的参数: [" + ex.getParameterName() + "]";
        log.error(LOG_FORMAT, ex.getClass().getName(), msg, error, HttpStatus.BAD_REQUEST);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, msg, error));
    }


    @ExceptionHandler(value = {DuplicateKeyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object duplicateKeyException(HttpServletRequest request, DuplicateKeyException e) {
        String error = "数据重复:" + (null != e.getCause() ? e.getCause().getMessage() : ""),
                msg = "出现重复数据,请检查参数是否正确?";
        log.error(LOG_FORMAT, e.getClass().getName(), msg, error, HttpStatus.BAD_REQUEST);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST, msg, error));
    }

    @ExceptionHandler(RemoteRequestException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public Object remoteRequestException(HttpServletRequest request, RemoteRequestException e) {
        final String res = request.getMethod().toUpperCase() + ":" + request.getRequestURI();
        String error = "Remote Service Failure:" + e.getMessage(), msg = "远程接口调用错误";
        log.error(LOG_FORMAT + "Code:{}\nURI:{}", e.getClass().getName(), error, error, HttpStatus.SERVICE_UNAVAILABLE, e.getStatus(), res);
        return dealError(request, ResponseVO.fail(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getStatus(), msg, error));
    }

    @ExceptionHandler(ValidateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Object handleValidateException(HttpServletRequest request, ValidateException e) {
        final String res = request.getMethod().toUpperCase() + ":" + request.getRequestURI();
        String error = e.getMessage();
        log.error(LOG_FORMAT + "Code:{}\nURI:{}", e.getClass().getName(), error, error, HttpStatus.BAD_REQUEST, -1, res);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST.value(), error, error));
    }

    @ExceptionHandler(ParamsInValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Object handleParamsInValidException(HttpServletRequest request, ParamsInValidException e) {
        final String res = request.getMethod().toUpperCase() + ":" + request.getRequestURI();
        String error = e.getMessage();
        log.error(LOG_FORMAT + "Code:{}\nURI:{}", e.getClass().getName(), error, error, HttpStatus.BAD_REQUEST, -1, res);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST.value(), error, error));
    }

    @ExceptionHandler(LuterException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleLuterException(HttpServletRequest request, LuterException e) {
        final String res = request.getMethod().toUpperCase() + ":" + request.getRequestURI();
        String error = e.getMessage();
        log.error(LOG_FORMAT + "Code:{}\nURI:{}", e.getClass().getName(), error, error, HttpStatus.INTERNAL_SERVER_ERROR, e.getCode(), res);
        return dealError(request, ResponseVO.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getCode(), error, error));
    }

    @ExceptionHandler(LuterIllegalParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Object handleLuterIllegalParameterException(HttpServletRequest request, LuterIllegalParameterException e) {
        String error = e.getMessage();
        final String res = request.getMethod().toUpperCase() + ":" + request.getRequestURI();
        log.error(LOG_FORMAT + "Code:{}\nURI:{}", e.getClass().getName(), error, error, HttpStatus.BAD_REQUEST, e.getCode(), res);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST.value(), e.getCode(), error, error));
    }

    @ExceptionHandler(LuterIdEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleLuterIdEntityNotFoundException(HttpServletRequest request, final LuterIdEntityNotFoundException e) {
        final String res = request.getMethod().toUpperCase() + ":" + request.getRequestURI();
        String error = e.getMessage();
        log.error(LOG_FORMAT + "Code:{}\nURI:{}", e.getClass().getName(), error, error, HttpStatus.BAD_REQUEST, e.getCode(), res);
        return dealError(request, ResponseVO.fail(HttpStatus.BAD_REQUEST.value(), e.getCode(), error, error));
    }


    protected Object dealError(HttpServletRequest request, ResponseVO<?> fail) {
        return dealError(request, fail, errorView);
    }

    protected Object dealError(HttpServletRequest request, ResponseVO<?> fail, String viewName) {
        //如果是json请求
        if (ResponseUtils.isJson(request)) {
            return ResponseEntity.status(fail.getStatus()).body(fail);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewName);
        modelAndView.setStatus(HttpStatus.valueOf(fail.getStatus()));
        modelAndView.addAllObjects(BeanUtil.beanToMap(fail));
        return modelAndView;
    }

    protected Throwable getRootExceptionThrowable(Exception e) {
        if (null == e) {
            return null;
        }
        Throwable rootCause = e;
        while (null != rootCause.getCause()) {
            rootCause = rootCause.getCause();
        }
        return rootCause;

    }
}