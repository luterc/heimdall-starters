package com.luter.heimdall.starter.jpa.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

@Slf4j
public abstract class AbstractTransactionAdviceConfig {
    public static final String AOP_POINTCUT_EXPRESSION = "execution (* com.luter..service..*.*(..))))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        /*事务管理规则，声明具备事务管理的方法名*/
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        //读写事务
        source.addTransactionalMethod("add*", requiredTransactionRule());
        source.addTransactionalMethod("save*", requiredTransactionRule());
        source.addTransactionalMethod("send*", requiredTransactionRule());
        source.addTransactionalMethod("insert*", requiredTransactionRule());
        source.addTransactionalMethod("update*", requiredTransactionRule());
        source.addTransactionalMethod("delete*", requiredTransactionRule());
        source.addTransactionalMethod("remove*", requiredTransactionRule());
        source.addTransactionalMethod("reset*", requiredTransactionRule());
        source.addTransactionalMethod("exec*", requiredTransactionRule());
        source.addTransactionalMethod("set*", requiredTransactionRule());
        //只读事务
        source.addTransactionalMethod("get*", readOnlyTransactionRule());
        source.addTransactionalMethod("query*", readOnlyTransactionRule());
        source.addTransactionalMethod("find*", readOnlyTransactionRule());
        source.addTransactionalMethod("list*", readOnlyTransactionRule());
        source.addTransactionalMethod("load*", readOnlyTransactionRule());
        source.addTransactionalMethod("count*", readOnlyTransactionRule());
        source.addTransactionalMethod("is*", readOnlyTransactionRule());
        log.warn("初始化 全局 Spring 事务自动注入切面");
        return new TransactionInterceptor((TransactionManager) transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        /* 声明切点的面
         * 切面（Aspect）：切面就是通知和切入点的结合。通知和切入点共同定义了关于切面的全部内容——它的功能、在何时和何地完成其功能。
         * */
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        /*声明和设置需要拦截的方法,用切点语言描写*/
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        /*设置切面=切点pointcut+通知TxAdvice*/
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

    private RuleBasedTransactionAttribute requiredTransactionRule() {
        RuleBasedTransactionAttribute required = new RuleBasedTransactionAttribute();

        /**添加对所有EXCEPTON异常进行事务回滚*/
        required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        /*PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是 默认值。 */
        required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        /*设置事务失效时间，如果超过5秒，则回滚事务*/
        required.setTimeout(TransactionDefinition.TIMEOUT_DEFAULT);

        return required;
    }

    private RuleBasedTransactionAttribute readOnlyTransactionRule() {
        RuleBasedTransactionAttribute readOnly = new RuleBasedTransactionAttribute();
        /*设置当前事务是否为只读事务，true为只读*/
        readOnly.setReadOnly(true);
        /* transactiondefinition 定义事务的隔离级别；
         * PROPAGATION_NOT_SUPPORTED事务传播级别5，以非事务运行，如果当前存在事务，则把当前事务挂起*/
        readOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);

        return readOnly;
    }

}
