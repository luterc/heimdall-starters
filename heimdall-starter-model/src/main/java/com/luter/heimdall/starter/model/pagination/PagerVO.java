package com.luter.heimdall.starter.model.pagination;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("公共分页和排序请求参数VO")
public class PagerVO implements Serializable {
    private static final long serialVersionUID = -1594248245183851665L;
    @ApiModelProperty("当前页码")
    private int page = 1;
    @ApiModelProperty("每页数量")
    private int size = 10;
    @ApiModelProperty(value = "当前页码", hidden = true)
    private List<SorterVO> orders = new ArrayList<>();

    public PagerVO setAsc(String... ascs) {
        removeOrder(SorterVO::isAsc);
        for (String s : ascs) {
            addOrder(SorterVO.asc(s));
        }
        return this;
    }

    public PagerVO setAscs(List<String> ascs) {
        return CollectionUtil.isNotEmpty(ascs) ? setAsc(ascs.toArray(new String[0])) : this;
    }

    public PagerVO setDescs(List<String> descs) {
        if (CollectionUtil.isNotEmpty(descs)) {
            removeOrder(item -> !item.isAsc());
            for (String s : descs) {
                addOrder(SorterVO.desc(s));
            }
        }
        return this;
    }

    public PagerVO setDesc(String... descs) {
        setDescs(Arrays.asList(descs));
        return this;
    }

    public PagerVO addOrder(List<SorterVO> items) {
        orders.addAll(items);
        return this;
    }

    public PagerVO addOrder(SorterVO... items) {
        orders.addAll(Arrays.asList(items));
        return this;
    }

    private void removeOrder(Predicate<SorterVO> filter) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            if (filter.test(orders.get(i))) {
                orders.remove(i);
            }
        }
    }

    public long getStart() {
        long l = (long) (page - 1) * (long) size;
        l = l < 0 ? 0 : l;
        return l;
    }

    public long getEnd() {
        return page * size;
    }
}
