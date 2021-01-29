package com.luter.heimdall.starter.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.luter.heimdall.starter.utils.exception.LuterIllegalParameterException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeUtils extends DateUtil {

    public static final String DATETIME_LONG = "yyyy-MM-dd HH:mm:ss";
    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public final static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public static boolean isValidDate(String str) {
        return isValidDate(str, DATETIME_LONG);
    }

    public static boolean isValidDate(String str, String format) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        if (StrUtil.isBlank(format)) {
            format = DATETIME_LONG;
        }
        boolean convertSuccess;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(str);
            convertSuccess = true;
        } catch (ParseException e) {
            throw new LuterIllegalParameterException("时间格式不正确");
        }
        return convertSuccess;
    }

    public static String getFriendlyTime(Date startTime, Date endTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endTime.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }
}
