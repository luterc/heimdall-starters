package com.luter.heimdall.starter.model.pagination;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "公共排序请求参数VO")
public class SorterVO implements Serializable {

    private static final long serialVersionUID = 5394433875231561986L;
    @ApiModelProperty(value = "排序字段")
    private String property;
    @ApiModelProperty(value = "是否正序(从小到大)")
    private boolean asc = true;

    public static SorterVO asc(String column) {
        return build(column, true);
    }

    public static SorterVO desc(String column) {
        return build(column, false);
    }

    public static List<SorterVO> ascs(String... columns) {
        return Arrays.stream(columns).map(SorterVO::asc).collect(Collectors.toList());
    }

    public static List<SorterVO> descs(String... columns) {
        return Arrays.stream(columns).map(SorterVO::desc).collect(Collectors.toList());
    }

    private static SorterVO build(String column, boolean asc) {
        return new SorterVO(column, asc);
    }

}
