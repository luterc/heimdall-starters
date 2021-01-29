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

package com.luter.heimdall.starter.model.pagination;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "公共分页返回数据格式")
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> implements Serializable {
    @ApiModelProperty("每页数量")
    private Integer pageSize;
    @ApiModelProperty("当前页码")
    private Integer pageNumber;
    @ApiModelProperty("数据总数")
    private Long totalCount;
    @ApiModelProperty("总页数")
    private Integer pageCount;
    @ApiModelProperty("返回记录总数")
    private Integer recordCount;
    @ApiModelProperty("是否为空")
    private Boolean empty;
    @ApiModelProperty("是否首页")
    private  Boolean first;
    @ApiModelProperty("是否末页")
    private Boolean last;
    @ApiModelProperty("列表数据")
    private List<T> records;

    public PageDTO(PagerVO page, long totalCount, List<T> records) {
        this.pageNumber = page.getPage();
        this.pageSize = page.getSize();
        this.records = records;
        this.totalCount = totalCount;
        this.pageCount = this.pageSize == 0 ? 1 : (int) Math.ceil((double) totalCount / (double) this.pageSize);
        this.first = this.pageNumber == 1;
        this.last = (this.pageNumber + 1) >= this.pageCount;
    }


    public PageDTO(int pageNo, int size, long totalCount, List<T> records) {
        this.pageNumber = pageNo;
        this.pageSize = size;
        this.records = records;
        this.totalCount = totalCount;
        this.pageCount = this.pageSize == 0 ? 1 : (int) Math.ceil((double) totalCount / (double) this.pageSize);
        this.first = this.pageNumber == 1;
        this.last = (this.pageNumber + 1) >= this.pageCount;
    }


}
