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

package com.luter.heimdall.starter.captcha.config.properties;

import lombok.Data;

@Data
public class GoogleCaptchaProperties {
    private int cType;
    private int width = 140;
    private int height = 40;
    private int digit = 4;
    private String border = "yes";
    private String borderColor = "105,179,90";
    private String fonts = "Arial,Courier,宋体,楷体,微软雅黑";
    private String fontColor = "black";
    private int fontSize = 32;

}
