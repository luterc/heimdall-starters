package com.luter.heimdall.starter.captcha.service;


import com.luter.heimdall.starter.captcha.dto.CaptchaDTO;

public interface CaptchaService {

    CaptchaDTO genCaptcha();

    CaptchaDTO genCaptcha(long expire);

    boolean checkCaptcha(String uuid, String code);

    boolean checkCaptcha(CaptchaDTO param);
}
