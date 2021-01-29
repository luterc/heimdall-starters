
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

package com.luter.heimdall.starter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
@Slf4j
@SuppressWarnings("unchecked")
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {
    private static ConfigurableListableBeanFactory beanFactory;
    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    public static <T> T getBean(String beanName, Class<T> requiredType) {
        if (containsBean(beanName)) {
            return getBeanFactory().getBean(beanName, requiredType);
        }
        return null;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return getBeanFactory().getBean(requiredType);
    }

    public static <T> T getBean(String beanName) {
        if (containsBean(beanName)) {
            Class<T> type = getType(beanName);
            return getBeanFactory().getBean(beanName, type);
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = null;
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(requestAttributes)) {
                request = requestAttributes.getRequest();
            }
        } catch (Exception ignored) {
        }
        return request;
    }

    public static boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    public static <T> Class<T> getType(String name) {
        return (Class<T>) getBeanFactory().getType(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;

    }
}
