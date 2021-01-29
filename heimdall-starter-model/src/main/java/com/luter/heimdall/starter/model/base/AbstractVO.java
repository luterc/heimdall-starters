package com.luter.heimdall.starter.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "公共请求参数定义")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractVO implements Serializable {
    @ApiModelProperty("主键ID")
    private Long id;
    @ApiModelProperty(value = "说明")
    private String remarks;


}
