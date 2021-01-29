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

package com.luter.heimdall.starter.jpa.base.service;


import org.hibernate.LockMode;
import org.hibernate.Session;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface HibernateService {

    Session getSession();

    <T> T getEntity(Class<T> entityName, Serializable id);

    <T> T getEntityById(Class<T> entityName, Serializable id);

    <T> T getEntityById(Class<T> entityName, Serializable id, boolean isThrowEx);

    <T> T getEntityByIdWithLock(Class<T> entityName, Serializable id, LockMode lockMode);

    <T> T findUniqueEntityByProperty(Class<T> entityClass, String propertyName, Object value);

    <T> List<T> findEntityByProperty(Class<T> entityClass, String propertyName, Object value);

    <T> Object saveEntity(T entity);

    <T> void updateEntity(T entity);

    <T> void updateBatchEntity(List<T> entities);

    <T> void saveBatchEntity(List<T> entities);

    <T> void saveOrUpdateEntity(T entity);

    <T> void deleteEntity(T entity);

    <T> int deleteBatch(final Class<T> entityClass, Object[] ids);


    <T> int deleteBatch(final Class<T> entityClass, Collection<Serializable> ids);

    <T> int deleteEntityById(Class<T> entityClass, Serializable id);

    int deleteBySql(final String deleteSql, final Object... paramlist);

    void deleteByIdWithSql(String tableName, String id) throws SQLException;

    void deleteByPropWithSql(String tableName, String prop, final Object[] values) throws SQLException;

    <T> int deleteByProperty(Class<T> entityClass, String propertyName, Object value);

    Boolean isExist(final String sql, final Object... paramlist);

    <T> Boolean isExist(Class<T> entityClass, String propertyName, Object value);

    <T> Long countAll(Class<T> entityClass);

    <T> List<T> listAll(final Class<T> entityClass);

    Long getCountBySql(final String countSql, final Object... paramlist);

    Long getCountByHql(final String countHql, final Object... paramlist);

    <T> Long getCountByProperty(Class<T> entityClass, String propertyName, Object value);

    List<Map<Object, Object>> listBySql(String sql, final Object... paramlist);

    <T> List<T> listBySql(Class<T> dto, String sql, final Object... paramlist);

    <T> List<T> listEntitiesBySql(Class<T> entityClass, String sql, final Object... paramlist);

    <T> List<T> listPageBySql(Class<T> dto, String sql, int page, int size, final Object... paramlist);

    <T> List<T> listEntityPageBySql(Class<T> entityClass, String sql, int page, int size, final Object... paramlist);

    <T> List<T> listByProperty(Class<T> entityClass, String propertyName, Object value);

    <T> List<T> listByHql(String hql, final Object... paramlist);

    <T> List<T> listPageByHql(String hql, final int page, final int size, final Object... paramlist);

    List<Map<Object, Object>> listPageBySql(String sql, int page, int size, final Object... paramlist);

    int executeUpdateWithHql(final String hql, final Object... paramlist);

    int executeUpdateWithSql(final String sql, final Object... paramlist);

    DatabaseMetaData getMeta() throws SQLException;

    boolean isTableExist(String tableName) throws SQLException;

}
