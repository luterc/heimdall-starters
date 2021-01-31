package com.luter.heimdall.starter.captcha.config.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * The type Easy captcha properties.
 *
 * @author Luter
 */
@Data
public class EasyCaptchaProperties {
    /**
     * 验证码类型，1：png图片，2：gif图片，3：中文png图片，4：中文gif，5：算术类型
     */
    private int cType;

    /**
     * 宽度
     */
    private int width = 120;
    /**
     * 高度
     */
    private int height = 40;
    /**
     * 位数
     */
    private int digit = 4;
    /**
     * 图形验证码类型
     * <p>
     * 1:数字字母混合、2：纯数字，3：纯字母，4：纯大写字母，5：纯小写字母，6：数字和大写字母
     */
    @Range(min = 1, max = 6, message = "验证码类型：{min}-{max}，必须数字")
    private int fType = 1;
    /**
     * 字体，1-10
     */
    @Range(min = 1, max = 10, message = "验证码字体：{min}-{max}，必须数字")
    private int font = 1;

    /**
     * 如果是算术类型验证码，生成几位数运算，默认2位
     */
    private int mathLength = 2;
}
