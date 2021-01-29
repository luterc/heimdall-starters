package com.luter.heimdall.starter.boot.validator.core;


import com.luter.heimdall.starter.boot.validator.annotations.ValidateParam;
import com.luter.heimdall.starter.boot.validator.annotations.ValidateParams;
import com.luter.heimdall.starter.utils.exception.ParamsCheckException;
import com.luter.heimdall.starter.utils.exception.ParamsInValidException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class CheckParamAspect {
    private static final int MAX_DEEP = 10;

    @Pointcut("@annotation(com.luter.heimdall.starter.boot.validator.annotations.ValidateParam)")
    public void checkParam() {
    }

    @Pointcut("@annotation(com.luter.heimdall.starter.boot.validator.annotations.ValidateParam.List)")
    public void checkParamList() {
    }

    @Pointcut("@annotation(com.luter.heimdall.starter.boot.validator.annotations.ValidateParams)")
    public void checkParams() {
    }


    @Around("checkParam()")
    public Object checkSingle(ProceedingJoinPoint point) throws Throwable {
        return interCheck(point, false, false);
    }


    @Around("checkParams()")
    public Object checkGroup(ProceedingJoinPoint point) throws Throwable {
        return interCheck(point, true, false);
    }

    @Around("checkParamList()")
    public Object checkList(ProceedingJoinPoint point) throws Throwable {
        return interCheck(point, true, true);
    }

    private Object interCheck(ProceedingJoinPoint point, boolean multi, boolean special) throws Throwable {
        Object obj;
        // 参数校验
        SampleResult sampleResult = doCheck(point, multi, special);
        if (!sampleResult.getPass()) {
            throw new ParamsInValidException(sampleResult.getMsg());
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }

    private SampleResult doCheck(JoinPoint point, boolean multi, boolean special) {
        Method method = this.getMethod(point);
        String[] paramName = this.getParamName(point);
        // 获取接口传递的所有参数
        Object[] arguments = point.getArgs();
        Boolean isValid = true;
        StringBuilder msg = new StringBuilder(" ");
        if (multi) {
            List<AnnoModel> annoModels;
            //多个参数校验  AOP监听带注解的方法，所以不用判断注解是否为空
            ValidateParams annotation = null;
            if (special) {
                // javac 编译后的注解 查看class文件可知 自动生成此类型（jdk8新特性）
                ValidateParam.List annotationList = method.getAnnotation(ValidateParam.List.class);
                annoModels = Arrays.stream(annotationList.value()).map(e -> new AnnoModel(true, e)
                ).collect(Collectors.toList());
            } else {
                annotation = method.getAnnotation(ValidateParams.class);
                annoModels = Arrays.stream(annotation.value()).map(e -> new AnnoModel(false, e)
                ).collect(Collectors.toList());
            }
            assert annoModels.size() > 0;
            List<AnnoModel> list = new ArrayList<>(annoModels);

            boolean shortPath = false;
            boolean anded = true;
            if (annotation != null) {
                shortPath = annotation.shortPath();
                anded = annotation.anded();
            }
            int passCounter = 0;
            for (AnnoModel annoModel : list) {
                ValidateParam anno = annoModel.getValidateParam();
                boolean required = anno.required();
                String argName = anno.argName();
                //参数值
                Object value = this.getParamValue(arguments, paramName, argName);
                if (!required && value == null) {
                    continue;
                }

                //调用枚举类的 CheckUtil类方法
                Boolean tmpValid = anno.value().func.apply(value, anno.express());
                if (tmpValid && !annoModel.getSed()) {
                    passCounter++;
                }
                if (isValid) {
                    isValid = tmpValid;
                }
                // 执行判断
                if (!tmpValid) {
                    // 只要有一个参数判断不通过，立即返回
                    String tmpMsg = anno.msg();
                    msg.append("  ").append(tmpMsg);
                    if ("".equals(tmpMsg)) {
                        msg.append("  ").append(argName).append(": ").append(anno.value().msg).append(" ").append(anno.express()).append(" ; ");
                    }
                    if (shortPath) {
                        break;
                    }
                }
            }

            if (!special && !anded && passCounter > 0) {
                msg.append(" 条件[或] ");
            }

            // 或条件成立
            if (!anded && list.size() > 1 && passCounter > 0) {
                isValid = true;
            }

        } else {
            // 单个参数校验
            // AOP监听带注解的方法，所以不用判断注解是否为空
            ValidateParam anno = method.getAnnotation(ValidateParam.class);
            boolean required = anno.required();
            String argName = anno.argName();
            //参数值
            Object value = this.getParamValue(arguments, paramName, argName);
            if (required || value != null) {
                // 执行判断 // 调用枚举类的 CheckUtil类方法
                isValid = anno.value().func.apply(value, anno.express());
                msg = new StringBuilder(anno.msg());
                if ("".equals(msg.toString())) {
                    msg = new StringBuilder(argName + ": " + anno.value().msg + " " + anno.express());
                }
            }
        }
        return new SampleResult(msg.toString(), isValid);

    }


    private String[] getParamName(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getParameterNames();
    }


    private Object getParamValue(Object[] arguments, String[] paramName, String argName) {
        Object value = null;
        String name = argName;
        String spliter = ".";
        if (argName.contains(spliter)) {
            name = argName.split("\\.")[0];
        }
        int index = 0;
        for (String string : paramName) {
            if (string.equals(name)) {
                //基本类型取值	// 不做空判断，如果注解配置的参数名称不存在，则取值为null
                value = arguments[index];
                break;
            }
            index++;
        }
        //从对象中取值
        if (argName.contains(spliter)) {
            //从实体对象中取值 理论上支持无限级  如 user.tokenObj.value
            String[] argNames = argName.split("\\.");
            try {

                if (argNames.length > MAX_DEEP) {
                    throw new ParamsCheckException(String.format(" occured validated raw error   argName {%s} the recursion is too deep,over 10..  ",
                            argName));
                }
                value = getObjValue(1, value, argNames);

            } catch (NoSuchMethodException e) {
                throw new ParamsCheckException(String.format(" occured validated raw error NoSuchMethodException  argName {%s} can not  found getter method  e is {%s}",
                        argName, e.getMessage()));
            } catch (InvocationTargetException e) {
                throw new ParamsCheckException(String.format("occured validated raw error InvocationTargetException  argName {%s} invoke getter method error  e is {%s}",
                        argName, e.getMessage()));
            } catch (IllegalAccessException e) {
                throw new ParamsCheckException(String.format("  validated raw error argName {%s} when" +
                        " get filed value IllegalAccessException occured   e is {%s}", argName, e.getMessage()));
            }
        }
        return value;
    }


    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint
                        .getTarget()
                        .getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(),
                                method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                System.err.println(this.getClass().getName() + ":" + e.getMessage());
            }
        }
        return method;
    }

    private static Object getObjValue(int index, Object value, String[] argNames) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        assert index > 0;
        assert argNames != null;
        if (index >= argNames.length || value == null) {
            return value;
        }

        String tempName = argNames[index];
        // 注意 调用公共的getter方法;
        String suffix = tempName.substring(0, 1).toUpperCase() + tempName.substring(1);
        String filedMethodName = "get" + suffix;
        boolean has = hasThisMethodByName(value, filedMethodName);
        //某些bool值命名不规范时 可能会找不到对应方法,导致失败。
        if (!has) {
            filedMethodName = "is" + suffix;
        }
        Method getterMethod = value.getClass().getMethod(filedMethodName);
        Object tempValue = getterMethod.invoke(value);

        return getObjValue(index + 1, tempValue, argNames);
    }

    private static boolean hasThisMethodByName(Object obj, String filedMethodName) {
        if (obj == null || filedMethodName == null) {
            return false;
        }

        Method[] methods = obj.getClass().getMethods();
        if (methods.length <= 0) {
            return false;
        }
        for (Method method : methods) {
            if (filedMethodName.equals(method.getName())) {
                return true;
            }
        }
        return false;
    }

    private static class SampleResult {
        private final String msg;
        private final Boolean pass;


        public SampleResult(String msg, Boolean pass) {
            this.msg = msg;
            this.pass = pass;
        }

        public String getMsg() {
            return msg;
        }


        public Boolean getPass() {
            return pass;
        }

    }

    private static class AnnoModel {

        private final Boolean sed;
        private final ValidateParam validateParam;

        public AnnoModel(boolean sed, ValidateParam validateParam) {
            this.sed = sed;
            this.validateParam = validateParam;
        }

        public Boolean getSed() {
            return sed;
        }

        public ValidateParam getValidateParam() {
            return validateParam;
        }

    }

}
