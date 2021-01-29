package com.luter.heimdall.starter.boot.validator.config;

import com.luter.heimdall.starter.boot.validator.core.CheckParamAspect;
import com.luter.heimdall.starter.utils.SpringUtils;
import org.springframework.context.annotation.Import;

@Import({CheckParamAspect.class, SpringUtils.class})
public class SpringValidateAutoConfig {
}
