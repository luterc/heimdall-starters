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

package com.luter.heimdall.starter.captcha.config.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class EasyCaptchaProperties {
    private int cType;

    private int width = 120;
    private int height = 40;
    private int digit = 4;
    @Range(min = 1, max = 6, message = "验证码类型：{min}-{max}，必须数字")
    private int fType = 1;
    @Range(min = 1, max = 10, message = "验证码字体：{min}-{max}，必须数字")
    private int font = 1;

    private int mathLength = 2;
}
