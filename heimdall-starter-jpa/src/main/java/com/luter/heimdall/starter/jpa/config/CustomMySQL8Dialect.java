package com.luter.heimdall.starter.jpa.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

@Slf4j
public class CustomMySQL8Dialect extends MySQL8Dialect {
    public CustomMySQL8Dialect() {
        super();
        log.debug("register MySQL8Dialect.....");
        registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.getName());
    }
}
