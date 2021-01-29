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
