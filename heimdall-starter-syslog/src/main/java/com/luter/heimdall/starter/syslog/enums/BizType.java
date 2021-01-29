package com.luter.heimdall.starter.syslog.enums;


public enum BizType {
    LIST(1, "条件分页列表查询"),
    INSERT(2, "新增数据"),

    UPDATE(3, "修改数据"),

    DELETE(4, "删除数据"),
    GRANT(5, "授权"),

    EXPORT(6, "导出数据"),

    IMPORT(7, "导入数据"),
    BATCH_DELETE(8, "批量删除数据"),
    DETAIL(9, "查看单条数据详情"),
    LOGIN(10, "登录"),
    LOGOUT(11, "注销"),
    KICKOUT(12, "踢出用户"),
    OTHER(-1, "其他");


    private final int value;


    private final String name;

    BizType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
