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

package com.luter.heimdall.starter.boot.validator.utils;


import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.luter.heimdall.starter.boot.validator.core.ParamValidator;
import com.luter.heimdall.starter.utils.exception.ParamsCheckException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("unchecked")
public class ValidateFunc {
    public static Boolean customValidate(Object value, String beanName) {
        ParamValidator<Object> bean = SpringUtil.getBean(beanName);
        Function<Object, Boolean> func = bean::validate;
        return func.apply(value);
    }


    public static Boolean isNull(Object value, String express) {
        if (null != value) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    public static Boolean isMobilePhone(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.MOBILE.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isNumber(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        if (value instanceof Number) {
            return Boolean.TRUE;
        }

        return Pattern.compile(RegexEnum.NUMBER.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isNotNull(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    public static Boolean isEmpty(Object value, String express) {
        return !isNotEmpty(value, express);
    }


    public static Boolean isNotEmpty(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String && "".equals(((String) value).trim())) {
            return Boolean.FALSE;
        }
        if (value instanceof Collection && ((Collection) value).size() == 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    public static Boolean isTrue(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            try {
                return Boolean.parseBoolean((String) value);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    public static Boolean isFalse(Object value, String express) {
        return !isTrue(value, express);
    }


    public static Boolean isDate(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd";
        }
        if (value instanceof String) {
            String v = ((String) value);
            try {
                LocalDate.parse(v, DateTimeFormatter.ofPattern(express));
                return Boolean.TRUE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return Boolean.TRUE;
        }
        if (value instanceof LocalDate) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    public static Boolean isDateTime(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd HH:mm:ss";
        }
        // 通常json格式参数，都是以字符串类型传递，优先判断
        if (value instanceof String) {
            //.replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            String v = ((String) value);
            try {
                LocalDateTime.parse(v, DateTimeFormatter.ofPattern(express));
                return Boolean.TRUE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return Boolean.TRUE;
        }
        if (value instanceof LocalDateTime) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static Boolean isPast(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd HH:mm:ss";
        }
        // 通常json格式参数，都是以字符串类型传递，优先判断
        if (value instanceof String) {
            //.replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            String v = ((String) value);
            try {
                LocalDateTime ldt = LocalDateTime.parse(v, DateTimeFormatter.ofPattern(express));
                return LocalDateTime.now().isAfter(ldt);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().after((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().isAfter((LocalDate) value);
        }
        if (value instanceof LocalDateTime) {
            return LocalDateTime.now().isAfter((LocalDateTime) value);
        }
        return Boolean.FALSE;
    }

    public static Boolean isFuture(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd HH:mm:ss";
        }
        // 通常json格式参数，都是以字符串类型传递，优先判断
        if (value instanceof String) {
            // .replaceAll("[-/]", "");   验证参数，不能处理掉所有异常的符号
            String v = ((String) value);
            try {
                LocalDateTime ldt = LocalDateTime.parse(v, DateTimeFormatter.ofPattern(express));
                return LocalDateTime.now().isBefore(ldt);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().before((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().isBefore((LocalDate) value);
        }
        if (value instanceof LocalDateTime) {
            return LocalDateTime.now().isBefore((LocalDateTime) value);
        }
        return Boolean.FALSE;
    }


    public static Boolean isToday(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd HH:mm:ss";
        }
        // 通常json格式参数，都是以字符串类型传递，优先判断
        if (value instanceof String) {
            // .replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            String v = ((String) value);
            try {
                LocalDate ld = LocalDate.parse(v, DateTimeFormatter.ofPattern(express));
                return LocalDate.now().equals(ld);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().equals(value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().equals(value);
        }
        return Boolean.FALSE;
    }


    public static Boolean isEmail(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            return Pattern.compile(RegexEnum.EMAIL.regex()).matcher(String.valueOf(value)).matches();
        }
        return Boolean.FALSE;
    }

    public static Boolean inRange(Object value, String rangeStr) {
        if (isNull(value, rangeStr)) {
            return Boolean.FALSE;
        }
        if (null == rangeStr || "".equals(rangeStr)) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            Integer begin = Integer.valueOf(rangeStr.split(",")[0]);
            Integer end = Integer.valueOf(rangeStr.split(",")[1]);
            Integer v = ((Integer) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Long) {
            Long begin = Long.valueOf(rangeStr.split(",")[0]);
            Long end = Long.valueOf(rangeStr.split(",")[1]);
            Long v = ((Long) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Short) {
            Short begin = Short.valueOf(rangeStr.split(",")[0]);
            Short end = Short.valueOf(rangeStr.split(",")[1]);
            Short v = ((Short) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Float) {
            Float begin = Float.valueOf(rangeStr.split(",")[0]);
            Float end = Float.valueOf(rangeStr.split(",")[1]);
            Float v = ((Float) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Double) {
            double begin = Double.parseDouble(rangeStr.split(",")[0]);
            double end = Double.parseDouble(rangeStr.split(",")[1]);
            double v = ((Double) value);
            return begin <= v && v <= end;
        }
        if (value instanceof BigDecimal) {
            BigDecimal begin = new BigDecimal(rangeStr.split(",")[0]);
            BigDecimal end = new BigDecimal(rangeStr.split(",")[1]);
            BigDecimal v = ((BigDecimal) value);
            return begin.compareTo(v) <= 0 && v.compareTo(end) <= 0;
        }

        return Boolean.FALSE;
    }


    public static Boolean outRange(Object value, String rangeStr) {
        return !inRange(value, rangeStr);
    }


    public static Boolean inLength(Object value, String rangeStr) {
        if (isNull(value, rangeStr)) {
            return Boolean.FALSE;
        }
        if (null == rangeStr || "".equals(rangeStr)) {
            return Boolean.FALSE;
        }
        String splicer = ",";
        if (value instanceof String) {
            Integer begin;
            Integer end;
            if (!rangeStr.contains(splicer)) {
                begin = 0;
            } else {
                begin = Integer.valueOf(rangeStr.split(splicer)[0]);
            }
            if (begin == 0) {
                end = Integer.valueOf(rangeStr);
            } else {
                end = Integer.valueOf(rangeStr.split(splicer)[1]);
            }
            int v = ((String) value).length();
            return begin <= v && v <= end;
        }
        return Boolean.FALSE;
    }


    public static Boolean inEnum(Object value, String enumStr) {
        if (isNull(value, null)) {
            return Boolean.FALSE;
        }
        if (null == enumStr || "".equals(enumStr)) {
            return Boolean.FALSE;
        }
        String[] array = enumStr.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(array));
        return set.contains(value.toString());
    }


    public static Boolean isGreaterThan(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) > Integer.parseInt(express);
        }
        if (value instanceof Long) {
            return ((Long) value) > Long.parseLong(express);
        }
        if (value instanceof Short) {
            return ((Short) value) > Short.parseShort(express);
        }
        if (value instanceof Float) {
            return ((Float) value) > Float.parseFloat(express);
        }
        if (value instanceof Double) {
            return ((Double) value) > Double.parseDouble(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) > 0;
        }
        if (value instanceof String) {
            return ((String) value).length() > Integer.parseInt(express);
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() > Integer.parseInt(express);
        }
        return Boolean.FALSE;
    }


    public static Boolean isGreaterThanEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) >= Integer.parseInt(express);
        }
        if (value instanceof Long) {
            return ((Long) value) >= Long.parseLong(express);
        }
        if (value instanceof Short) {
            return ((Short) value) >= Short.parseShort(express);
        }
        if (value instanceof Float) {
            return ((Float) value) >= Float.parseFloat(express);
        }
        if (value instanceof Double) {
            return ((Double) value) >= Double.parseDouble(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) >= 0;
        }
        if (value instanceof String) {
            return ((String) value).length() >= Integer.parseInt(express);
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() >= Integer.parseInt(express);
        }
        return Boolean.FALSE;

    }

    public static Boolean isLessThan(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) < Integer.parseInt(express);
        }
        if (value instanceof Long) {
            return ((Long) value) < Long.parseLong(express);
        }
        if (value instanceof Short) {
            return ((Short) value) < Short.parseShort(express);
        }
        if (value instanceof Float) {
            return ((Float) value) < Float.parseFloat(express);
        }
        if (value instanceof Double) {
            return ((Double) value) < Double.parseDouble(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) < 0;
        }
        if (value instanceof String) {
            return ((String) value).length() < Integer.parseInt(express);
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() < Integer.parseInt(express);
        }
        return Boolean.FALSE;
    }

    public static Boolean isLessThanEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) <= Integer.parseInt(express);
        }
        if (value instanceof Long) {
            return ((Long) value) <= Long.parseLong(express);
        }
        if (value instanceof Short) {
            return ((Short) value) <= Short.parseShort(express);
        }
        if (value instanceof Float) {
            return ((Float) value) <= Float.parseFloat(express);
        }
        if (value instanceof Double) {
            return ((Double) value) <= Double.parseDouble(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) <= 0;
        }
        if (value instanceof String) {
            return ((String) value).length() <= Integer.parseInt(express);
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() <= Integer.parseInt(express);
        }
        return Boolean.FALSE;
    }

    public static Boolean isNotEqual(Object value, String express) {
        return !isEqual(value, express);
    }

    public static Boolean isSuitableFileLength(Object value, String express) {
        if (value == null || "".equals(value)) {
            return Boolean.FALSE;
        }
        List<Long> lens = new ArrayList<>();
        if (value instanceof File || value instanceof MultipartFile) {
            judgeLen(value, lens);
        }
        if (value instanceof Collection) {
            ((Collection) value).forEach(e -> judgeLen(e, lens));
        }
        if (value.getClass().isArray()) {
            assert value instanceof Object[];
            Object[] objs = (Object[]) value;
            for (Object tmpValue : objs) {
                judgeLen(tmpValue, lens);
            }
        }
        if (lens.size() == 0) {
            return Boolean.FALSE;
        }

        return realJudgeFileLen(lens, express);

    }

    private static Boolean realJudgeFileLen(List<Long> lens, String express) {
        long defaultLen = RegexConstants.DEFAULT_FILE_SIZE;
        if (express != null && !"".equals(express)) {
            defaultLen = Long.parseLong(express);
        }

        // 单位 KB
        for (Long len : lens) {
            if (len / 1024 > defaultLen) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }


    public static Boolean isSuitableFileSuffix(Object value, String express) {
        if (value == null || "".equals(value)) {
            return Boolean.FALSE;
        }

        List names = new ArrayList<>();
        if (value instanceof File || value instanceof MultipartFile || value instanceof String) {
            judge(value, names);
        }
        if (value instanceof Collection) {
            ((Collection) value).forEach(e -> judge(e, names));
        }
        if (value.getClass().isArray()) {
            Object[] objs = (Object[]) value;
            for (int i = 0; i < objs.length; i++) {
                Object tmpvalue = objs[i];
                judge(tmpvalue, names);
            }
        }
        if (names.size() == 0) {
            return Boolean.FALSE;
        }

        return realJudgeFileSuffix(names, express);
    }

    private static Boolean realJudgeFileSuffix(List names, String express) {
        String[] suffixs;
        if (express != null && !"".equals(express)) {
            suffixs = express.split(",");
        } else {
            suffixs = RegexConstants.DEFAULT_ALLOWED_EXTENSION;
        }
        List<String> suffixList = Arrays.asList(suffixs);
        for (Object fileName : names) {
            String fileName1 = (String) fileName;
            if (fileName1.contains(".")) {
                String fileSuffix = fileName1.split("\\.")[1];
                if (!(fileSuffix != null && suffixList.contains(fileSuffix))) {
                    return Boolean.FALSE;
                }
            } else {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private static void judgeLen(Object tmpvalue, List names) {
        if (tmpvalue instanceof File) {
            long length = ((File) tmpvalue).length();
            names.add(length);
        } else if (tmpvalue instanceof MultipartFile) {
            long size = ((MultipartFile) tmpvalue).getSize();
            names.add(size);
        } else {
            throw new ParamsCheckException("the field type is wrong, we need a File or  MultipartFile ");
        }
    }

    private static void judge(Object tmpvalue, List names) {
        if (tmpvalue instanceof File) {
            String filename = ((File) tmpvalue).getName();
            names.add(filename);
        } else if (tmpvalue instanceof MultipartFile) {
            String filename = ((MultipartFile) tmpvalue).getOriginalFilename();
            names.add(filename);
        } else if (tmpvalue instanceof String) {
            names.add(tmpvalue);
        } else {
            throw new ParamsCheckException("the field type is wrong, we need a File or  MultipartFile or String ");
        }
    }


    public static Boolean isEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            return value.equals(express);
        }
        if (value instanceof Integer) {
            return value.equals(Integer.valueOf(express));
        }
        if (value instanceof Long) {
            return value.equals(Long.valueOf(express));
        }
        if (value instanceof Short) {
            return value.equals(Short.valueOf(express));
        }
        if (value instanceof Float) {
            return value.equals(Float.valueOf(express));
        }
        if (value instanceof Double) {
            return value.equals(Double.valueOf(express));
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) == 0;
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() == Integer.valueOf(express);
        }
        return Boolean.FALSE;
    }


    public static Boolean isPattern(Object value, String regEx) {
        if (isNull(value, null)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher((String) value);
            if (m.matches()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    public static Boolean isTimeMillSeconds(Object value, String regEx) {
        Boolean number = isNumber(value, regEx);
        if (number) {
            if (String.valueOf(value).length() == 13) {
                return true;
            }
        }
        return false;

    }

    public static Boolean isIdCard(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return IdcardUtil.isValidCard(value.toString());
    }


    public static Boolean isCreditCode(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Validator.isCreditCode(String.valueOf(value));
    }

    public static Boolean isChinesePostCode(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.ZIP_CODE.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isIpv4(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.IPV4.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isIpv6(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.IPV6.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isChinese(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.CHINESE.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isGeneral(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.GENERAL.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isMac(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.MAC_ADDRESS.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isPlateNumber(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.PLATE_NUMBER.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isUrl(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        try {
            new java.net.URL(String.valueOf(value));
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("warn")
    public static Boolean isISBN(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.ISBN.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isBankNumber(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        String number = String.valueOf(value);
        if (number.length() != 16 && number.length() != 19) {
            return false;
        }
        if (!number.matches("\\d+")) {
            return false;
        }

        char[] digits = number.toCharArray();
        int len = number.length();
        int numSum = 0;
        for (int i = len - 1, j = 1; i >= 0; i--, j++) {
            int value0 = digits[i] - '0';
            if (j % 2 == 0) {
                value0 *= 2;
                if (value0 > 9) {
                    value0 -= 9;
                }
            }
            numSum += value0;
        }
        return numSum % 10 == 0;
    }

    @SuppressWarnings("warn")
    public static Boolean isUUID(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.UUID.regex()).matcher(String.valueOf(value)).matches()
                || Pattern.compile(RegexEnum.UUID_SIMPLE.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isBirthday(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        final Matcher matcher = Pattern.compile(RegexEnum.BIRTHDAY.regex()).matcher(String.valueOf(value));
        if (matcher.find()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(3));
            int day = Integer.parseInt(matcher.group(5));
            // 验证年
            Calendar calendar = Calendar.getInstance();
            int thisYear = calendar.get(Calendar.YEAR);
            if (year < 1900 || year > thisYear) {
                return false;
            }

            // 验证月
            if (month < 1 || month > 12) {
                return false;
            }

            // 验证日
            if (day < 1 || day > 31) {
                return false;
            }
            // 检查几个特殊月的最大天数
            if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static Boolean isMobieExact(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.MOBILE_EXACT.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isMobieSimple(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.MOBILE_SIMPLE.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isUsername(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.USERNAME.regex()).matcher(String.valueOf(value)).matches();
    }


    public static Boolean isPassword(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.PASSWORD.regex()).matcher(String.valueOf(value)).matches();
    }

    public static Boolean isPasswordSimple(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return Pattern.compile(RegexEnum.PASSWORD_SIMPLE.regex()).matcher(String.valueOf(value)).matches();
    }


}
