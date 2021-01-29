package com.luter.heimdall.starter.captcha.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "验证码信息DTO")
public class CaptchaDTO implements Serializable {
    @ApiModelProperty("图形验证码唯一标识")
    private String uuid;
    @JsonIgnore
    private String code;
    @ApiModelProperty("验证码")
    private String captcha;
    @ApiModelProperty("base64编码的图片")
    private String content;
}
