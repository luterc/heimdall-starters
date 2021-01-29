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

package com.luter.heimdall.starter.boot.validator.enums;


import com.luter.heimdall.starter.boot.validator.utils.ValidateFunc;

import java.util.function.BiFunction;

public enum Validater {
    Custom("参数验证不通过", ValidateFunc::customValidate),

    Null("参数必须为空", ValidateFunc::isNull),

    NotNull("参数必须不为空", ValidateFunc::isNotNull),

    Empty("参数的必须为空", ValidateFunc::isEmpty),

    NotEmpty("参数必须非空", ValidateFunc::isNotEmpty),

    True("参数必须为 true", ValidateFunc::isTrue),

    False("参数必须为 false", ValidateFunc::isFalse),

    Date("参数必须是一个日期 yyyy-MM-dd", ValidateFunc::isDate),

    DateTime("参数必须是一个日期时间 yyyy-MM-dd HH:mm:ss", ValidateFunc::isDateTime),

    TimeMillSeconds("参数必须是一个时间毫秒值", ValidateFunc::isTimeMillSeconds),

    Past("参数必须是一个过去的日期", ValidateFunc::isPast),

    Future("参数必须是一个将来的日期", ValidateFunc::isFuture),

    Today("参数必须今天的日期", ValidateFunc::isToday),

    Enum("参数必须在枚举中", ValidateFunc::inEnum),

    Email("参数必须是Email地址", ValidateFunc::isEmail),

    MobilePhone("手机号码格式错误,支持中国大陆地区手机号码", ValidateFunc::isMobilePhone),
    MobileSimple("手机号码格式错误,支持中国大陆地区手机号码", ValidateFunc::isMobieSimple),
    MobileExact("手机号码格式错误,支持中国大陆地区手机号码", ValidateFunc::isMobieExact),
    Username("用户名由字母数字下划线组成且开头必须是字母，不超过16位", ValidateFunc::isUsername),
    Password("密码至少要由包括大小写字母、数字、标点符号的其中两种,8-16位", ValidateFunc::isPassword),

    Number("参数必须是数字类型", ValidateFunc::isNumber),

    Range("参数必须在合适的范围内", ValidateFunc::inRange),

    NotIn("参数必须不在指定的范围内", ValidateFunc::outRange),

    Length("参数长度必须在指定范围内", ValidateFunc::inLength),

    gt("参数必须大于指定值", ValidateFunc::isGreaterThan),

    lt("参数必须小于指定值", ValidateFunc::isLessThan),

    ge("参数必须大于等于指定值", ValidateFunc::isGreaterThanEqual),

    le("参数必须小于等于指定值", ValidateFunc::isLessThanEqual),

    ne("参数必须不等于指定值", ValidateFunc::isNotEqual),

    eq("参数必须等于指定值", ValidateFunc::isEqual),

    Pattern("参数必须符合指定的正则表达式", ValidateFunc::isPattern),

    Chinese("参数必须是汉字", ValidateFunc::isChinese),

    isUrl("参数必须是url", ValidateFunc::isUrl),

    isISBN("参数必须是一个书籍ISBN编号", ValidateFunc::isISBN),

    isBankNumber("参数必须是一个银行卡号", ValidateFunc::isBankNumber),

    isChinesePostCode("参数必须是中国邮编", ValidateFunc::isChinesePostCode),

    isPlateNumber("参数必须是中国车牌号", ValidateFunc::isPlateNumber),

    isUUID("参数必须是UUID", ValidateFunc::isUUID),

    isIpv4("参数必须是ipv4", ValidateFunc::isIpv4),

    isIpv6("参数必须是ipv6", ValidateFunc::isIpv6),

    isMac("参数必须是mac地址", ValidateFunc::isMac),

    isIDCard("参数必须是身份证", ValidateFunc::isIdCard),

    isGeneral("参数必须是英文字母,数字和下划线", ValidateFunc::isGeneral),

    isBirthdaystr("参数必须是生日字符串格式", ValidateFunc::isBirthday),

    isSuitableFileLength("文件太大啦", ValidateFunc::isSuitableFileLength),

    isSuitableFileSuffix("文件必须是有效合法后缀的文件", ValidateFunc::isSuitableFileSuffix);


    public String msg;

    public BiFunction<Object, String, Boolean> func;

    Validater(String msg, BiFunction<Object, String, Boolean> func) {
        this.msg = msg;
        this.func = func;
    }
}