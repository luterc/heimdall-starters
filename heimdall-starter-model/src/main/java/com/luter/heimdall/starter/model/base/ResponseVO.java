
package com.luter.heimdall.starter.model.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@ApiModel("全局返回数据")
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVO<T> {
    public static final Integer DEFAULT_CODE = -1;

    @ApiModelProperty("返回数据")
    private T data;
    @ApiModelProperty("成功失败的提示消息")
    private String msg;
    @ApiModelProperty("异常和错误信息,用作调试")
    private String error;

    @ApiModelProperty("时间戳")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime timestamp = LocalDateTime.now();
    @ApiModelProperty(value = "http状态码。对应HttpStatus状态，如:200、401、403、500等")
    private Integer status;
    @ApiModelProperty("业务状态码,用以标识不同业务状态")
    private Integer code;

    public static <T> ResponseVO<T> ok() {
        return build(HttpStatus.OK.value(), DEFAULT_CODE, "success", "", null);
    }

    public static <T> ResponseVO<T> ok(T data) {
        return build(HttpStatus.OK.value(), DEFAULT_CODE, "success", "success", data);
    }

    public static <T> ResponseVO<T> ok(String msg) {
        return build(HttpStatus.OK.value(), DEFAULT_CODE, msg, "", null);
    }

    public static <T> ResponseVO<T> ok(String msg, T data) {
        return build(HttpStatus.OK.value(), DEFAULT_CODE, msg, "", data);
    }

    public static <T> ResponseVO<T> ok(HttpStatus status, String msg) {
        return build(HttpStatus.OK.value(), DEFAULT_CODE, msg, "", null);
    }

    public static <T> ResponseVO<T> fail(String msg) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR.value(), DEFAULT_CODE, msg, "", null);
    }


    public static <T> ResponseVO<T> fail(Integer status, String msg) {
        return build(status, DEFAULT_CODE, msg, "", null);
    }

    public static <T> ResponseVO<T> fail(HttpStatus status, String msg) {
        return build(status.value(), DEFAULT_CODE, msg, "", null);
    }


    public static <T> ResponseVO<T> badRequest(String msg) {
        return build(HttpStatus.FORBIDDEN.value(), DEFAULT_CODE, msg, "bad request", null);
    }

    public static <T> ResponseVO<T> fail(HttpStatus status, String msg, String error) {
        return build(status.value(), DEFAULT_CODE, msg, error, null);
    }

    public static <T> ResponseVO<T> fail(String msg, T data) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR.value(), 0, msg, "", data);
    }

    public static <T> ResponseVO<T> fail(HttpStatus status, String msg, T data) {
        return build(status.value(), DEFAULT_CODE, msg, "", data);
    }

    public static <T> ResponseVO<T> fail(HttpStatus status, int code, String msg, T data) {
        return build(status.value(), code, msg, "", data);
    }

    public static <T> ResponseVO<T> fail(Integer status, String msg, String error) {
        return build(status, DEFAULT_CODE, msg, error, null);
    }

    public static <T> ResponseVO<T> fail(Integer status, Integer code, String msg, String error) {
        return build(status, code, msg, error, null);
    }

    public static <T> ResponseVO<T> fail(Integer status, Integer code, String msg) {
        return build(status, code, msg, "", null);
    }

    public static <T> ResponseVO<T> fail(HttpStatus status, Integer code, String msg) {
        return build(status.value(), code, msg, "", null);
    }

    public static <T> ResponseVO<T> fail(Integer status, String msg, String error, T data) {
        return build(status, DEFAULT_CODE, msg, error, data);
    }

    public static <T> ResponseVO<T> fail(Integer status, Integer code, String msg, String error, T data) {
        return build(status, code, msg, error, data);
    }

    private static <T> ResponseVO<T> build(Integer status, Integer code, String msg, String error, T data) {
        ResponseVO<T> apiResult = new ResponseVO<>();
        apiResult.setStatus(status);
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        apiResult.setError(error);
        return apiResult;
    }


    @JsonIgnore
    protected boolean is1xxInformational() {
        return HttpStatus.valueOf(this.status).is1xxInformational();
    }

    @JsonIgnore
    protected boolean is2xxSuccessful() {
        return HttpStatus.valueOf(this.status).is2xxSuccessful();
    }

    @JsonIgnore
    protected boolean is3xxRedirection() {
        return HttpStatus.valueOf(this.status).is3xxRedirection();
    }

    @JsonIgnore
    protected boolean is4xxClientError() {
        return HttpStatus.valueOf(this.status).is4xxClientError();
    }

    @JsonIgnore
    protected boolean is5xxServerError() {
        return HttpStatus.valueOf(this.status).is5xxServerError();
    }

    @JsonIgnore
    protected boolean isClientAndServerError() {
        return this.is4xxClientError() || this.is5xxServerError();
    }

}
