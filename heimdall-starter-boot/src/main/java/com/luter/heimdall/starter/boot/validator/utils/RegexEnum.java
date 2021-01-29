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

package com.luter.heimdall.starter.boot.validator.utils;

public enum RegexEnum {

    USERNAME(RegexConstants.USERNAME, "用户名由字母数字下划线组成且开头必须是字母，不超过16位"),
    PASSWORD(RegexConstants.PASSWORD, "密码至少要由包括大小写字母、数字、标点符号的其中两种,8-16位"),
    PASSWORD_SIMPLE(RegexConstants.PASSWORD_SIMPLE, "以字母开头，长度在6~18之间，包含字符、数字和下划线"),
    MOBILE_EXACT(RegexConstants.MOBILE_EXACT, "手机号码格式,请输入正确的大陆地区手机号码"),
    MOBILE_SIMPLE(RegexConstants.MOBILE_SIMPLE, "手机号码格式,请输入正确的大陆地区手机号码"),
    CHAR(RegexConstants.CHAR, "输入由26个英文字母组成的字符串"),

    UPPER_CHAR(RegexConstants.UPPER_CHAR, "输入由26个大写英文字母组成的字符串"),
    LOWER_CHAR(RegexConstants.LOWER_CHAR, "输入由26个小写英文字母组成的字符串"),
    CHAR_NUMBER(RegexConstants.CHAR_NUMBER, "输入由数字、26个英文字母或者下划线组成的字符串"),
    CHINESE_CHAR(RegexConstants.CHINESE_CHAR, "只能输入汉字"),
    EMAIL(RegexConstants.EMAIL, "邮箱格式错误"),
    GENERAL(RegexConstants.GENERAL, "英文字母 、数字和下划线"),
    CHINESE(RegexConstants.CHINESE, "只能输入汉字"),

    IPV4(RegexConstants.IPV4, "错误的ip地址，请输入正确的IPV4地址"),

    IPV6(RegexConstants.IPV6, "错误的ip地址，请输入正确的IPV6地址"),

    MONEY(RegexConstants.MONEY, "货币金额格式错误"),

    MOBILE(RegexConstants.MOBILE, "请输入正确的中国大陆地区手机号码"),

    ID_CARD_18(RegexConstants.CITIZEN_ID, "请输入正确的18位身份证号码"),
    ZIP_CODE(RegexConstants.ZIP_CODE, "邮编格式错误"),
    BIRTHDAY(RegexConstants.BIRTHDAY, "生日日期格式错误"),
    GENERAL_WITH_CHINESE(RegexConstants.GENERAL_WITH_CHINESE, "只能输入:汉字、英文字母、数字和下划线"),
    ISBN(RegexConstants.ISBN, "请输入正确的ISBN编码，支持ISBN-10或者ISBN-13"),

    UUID(RegexConstants.UUID, "错误的UUID格式"),
    UUID_SIMPLE(RegexConstants.UUID_SIMPLE, "错误的UUID格式"),
    MAC_ADDRESS(RegexConstants.MAC_ADDRESS, "错误的Mac地址"),

    PLATE_NUMBER(RegexConstants.PLATE_NUMBER, "车牌号码错误"),
    CREDIT_CODE(RegexConstants.CREDIT_CODE, "社会统一信用代码错误"),
    NUMBER(RegexConstants.NUMBER, "只能输入数字");

    private final String regex;
    private final String msg;

    RegexEnum(String regex, String msg) {
        this.regex = regex;
        this.msg = msg;
    }

    public String regex() {
        return regex;
    }

    public String msg() {
        return msg;
    }
}
