package com.luter.heimdall.starter.model.base;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel(value = "公共DTO参数")
@Data
@JsonIgnoreProperties({"lastModifiedTime", "password", "salt", "version", "createdTime", "createdBy", "lastModifiedBy"})
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractDTO implements Serializable {
    @ApiModelProperty("主键ID")
    private Long id;
    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @ApiModelProperty("创建者ID")
    private Long createdBy;
    @ApiModelProperty("最后修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedTime;
    @ApiModelProperty("最后修改者ID")
    private Long lastModifiedBy;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty(value = "说明")
    private String remarks;
}
