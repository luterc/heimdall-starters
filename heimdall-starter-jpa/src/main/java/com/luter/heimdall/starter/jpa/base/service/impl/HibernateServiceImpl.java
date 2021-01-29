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

package com.luter.heimdall.starter.jpa.base.service.impl;

import com.luter.heimdall.starter.jpa.base.service.HibernateService;
import com.luter.heimdall.starter.utils.exception.LuterIdEntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@SuppressWarnings("unchecked")
public class HibernateServiceImpl implements HibernateService {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public Session getSession() {
        Session session;
        if (entityManager == null || (session = entityManager.unwrap(Session.class)) == null) {
            throw new NullPointerException("获取entityManager失败");
        }
        session.setFlushMode(FlushModeType.COMMIT);
        return session;
    }

    @Override
    public <T> T getEntity(Class<T> entityName, Serializable id) {
        if (null == id || "".equals(id)) {
            return null;
        }
        return getSession().get(entityName, id);
    }


    @Override
    public <T> T getEntityById(final Class<T> entityName, final Serializable id) {
        return getEntityById(entityName, id, true);
    }

    @Override
    public <T> T getEntityById(final Class<T> entityName, final Serializable id, boolean isThrowEx) {
        if (null == id || "".equals(id)) {
            if (isThrowEx) {
                throw new LuterIdEntityNotFoundException("获取数据失败:请提供ID");
            } else {
                return null;
            }
        }
        T t = getSession().get(entityName, id);
        if (null == t) {
            if (isThrowEx) {
                throw new LuterIdEntityNotFoundException("获取数据失败,未找到");
            } else {
                return null;
            }

        }
        return t;
    }

    @Override
    public <T> T getEntityByIdWithLock(Class<T> entityName, Serializable id, LockMode lockMode) {
        if (null == id || "".equals(id)) {
            return null;
        }
        return getSession().get(entityName, id, lockMode);
    }

    @Override
    public <T> T findUniqueEntityByProperty(Class<T> entityClass, String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(builder.equal(root.get(propertyName), value));
        Query<T> q = getSession().createQuery(query);
        return q.uniqueResult();
    }

    @Override
    public <T> List<T> findEntityByProperty(Class<T> entityClass, String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(builder.equal(root.get(propertyName), value));
        Query<T> q = getSession().createQuery(query);
        return q.getResultList();
    }

    @Override
    public <T> Object saveEntity(final T entity) {
        Object save = getSession().save(entity);
        getSession().flush();
        return save;

    }

    @Override
    public <T> void updateEntity(final T entity) {
        getSession().update(entity);
        getSession().flush();
    }

    @Override
    public <T> void updateBatchEntity(List<T> entities) {
        if (null != entities && entities.size() > 0) {
            Session session = getSession();
            for (int i = 0; i < entities.size(); i++) {
                session.update(entities.get(i));
                if (i % 100 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            session.flush();
            session.clear();
        }

    }

    @Override
    public <T> void saveBatchEntity(final List<T> entities) {
        if (null != entities && entities.size() > 0) {
            Session session = getSession();
            for (int i = 0; i < entities.size(); i++) {
                session.save(entities.get(i));
                if (i % 100 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            session.flush();
            session.clear();
        }
    }

    @Override
    public <T> void saveOrUpdateEntity(final T entity) {
        getSession().saveOrUpdate(entity);
        getSession().flush();
    }

    @Override
    public <T> void deleteEntity(final T entity) {
        getSession().delete(entity);
        getSession().flush();
    }

    @Override
    public <T> int deleteBatch(final Class<T> entityClass, final Object[] ids) {
        if (null == ids || ids.length == 0) {
            return 0;
        }
        String hql = String.format("delete from %s where id in (:ids) ", entityClass.getSimpleName());
        Query queryObject = getSession().createQuery(hql);
        queryObject.setParameterList("ids", ids);
        return queryObject.executeUpdate();
    }


    @Override
    public <T> int deleteBatch(final Class<T> entityClass, final Collection<Serializable> ids) {
        if (null == ids || ids.size() == 0) {
            return 0;
        }
        String hql = String.format("delete from %s where id in (:ids) ", entityClass.getSimpleName());
        Query queryObject = getSession().createQuery(hql);
        queryObject.setParameterList("ids", ids);
        return queryObject.executeUpdate();
    }

    @Override
    public <T> int deleteEntityById(final Class<T> entityClass, final Serializable Id) {
        if (null == Id) {
            throw new LuterIdEntityNotFoundException("ID不能为空");
        } else {
            String hql = String.format("delete from %s where id=%s", entityClass.getSimpleName(), Id);
            Query<?> queryObject = getSession().createQuery(hql);
            return queryObject.executeUpdate();
        }

    }

    @Override
    public int deleteBySql(String deleteSql, final Object... paramlist) {
        return executeUpdateWithSql(deleteSql, paramlist);
    }

    @Override
    public void deleteByIdWithSql(String tableName, String id) throws SQLException {
        if (isTableExist(tableName)) {
            String deleteSQL = "delete from " + tableName + " where id =?0";
            executeUpdateWithSql(deleteSQL, id);
        } else {
            throw new RuntimeException("table [ " + tableName + " ] is not exist ,please check !");
        }
    }

    @Override
    public void deleteByPropWithSql(String tableName, String prop, final Object[] values) throws SQLException {
        if (isTableExist(tableName)) {
            String deleteSQL = String.format("delete from %s where %s in(:ids)", tableName, prop);
            NativeQuery<Void> query = getSession().createNativeQuery(deleteSQL);
            query.setParameterList("ids", values);
            query.executeUpdate();
        } else {
            throw new RuntimeException("table [ " + tableName + " ] is not exist ,please check!");
        }
    }

    @Override
    public <T> int deleteByProperty(Class<T> entityClass, String propertyName, Object value) {
        String hql = "delete from " + entityClass.getSimpleName() + " where " + propertyName + " = ?0";
        return executeUpdateWithHql(hql, value);
    }

    @Override
    public Boolean isExist(String sql, Object... paramlist) {
        Long count = getCountBySql(sql, paramlist);
        return count > 0;
    }


    @Override
    public <T> Boolean isExist(Class<T> entityClass, String propertyName, Object value) {
        if (null == entityClass) {
            throw new RuntimeException("entity class can not be null");
        }
        return getCountByProperty(entityClass, propertyName, value) > 0;
    }

    @Override
    public <T> Long countAll(Class<T> entityClass) {
        String hql = "select count(*) from " + entityClass.getSimpleName();
        Query query = getSession().createQuery(hql);
        return (Long) query.uniqueResult();
    }

    @Override
    public <T> List<T> listAll(Class<T> entityClass) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        criteriaQuery.from(entityClass);
        return getSession().createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Long getCountBySql(final String countSql, final Object... paramlist) {
        Query query = getSession().createNativeQuery(countSql);
        setParameters(query, paramlist);
        BigInteger count = (BigInteger) query.uniqueResult();
        return count.longValue();
    }

    @Override
    public Long getCountByHql(String countHQL, final Object... paramlist) {
        Query query = getSession().createQuery(countHQL);
        setParameters(query, paramlist);
        return (Long) query.uniqueResult();
    }

    @Override
    public <T> Long getCountByProperty(Class<T> entityClass, String propertyName, Object value) {
        Assert.hasLength(propertyName, "属性名称不能为空");
        String hql = "select count(*) from " + entityClass.getSimpleName() + " where " + propertyName + " = ?0";
        return getCountByHql(hql, value);
    }


    @Override
    public List<Map<Object, Object>> listBySql(final String sql, final Object... paramlist) {
        Query query = getSession().createNativeQuery(sql);
        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        setParameters(nativeQuery, paramlist);
        nativeQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return nativeQuery.getResultList();
    }

    @Override
    public <T> List<T> listBySql(Class<T> dto, String sql, final Object... paramlist) {
        Query<T> query = getSession().createNativeQuery(sql);
        NativeQueryImpl<T> nativeQuery = (NativeQueryImpl<T>) query;
        setParameters(nativeQuery, paramlist);
        nativeQuery.setResultTransformer(Transformers.aliasToBean(dto));
        return nativeQuery.getResultList();
    }

    @Override
    public <T> List<T> listEntitiesBySql(Class<T> entityClass, String sql, Object... paramlist) {
        Query<T> query = getSession().createNativeQuery(sql);
        NativeQueryImpl<T> nativeQuery = (NativeQueryImpl<T>) query;
        nativeQuery.addEntity(entityClass);
        setParameters(nativeQuery, paramlist);
        return nativeQuery.getResultList();
    }

    @Override
    public <T> List<T> listPageBySql(Class<T> dto, String sql, int page, int size, final Object... paramlist) {
        Query<T> query = getSession().createNativeQuery(sql);
        NativeQueryImpl<T> nativeQuery = (NativeQueryImpl<T>) query;
        setParameters(nativeQuery, paramlist);
        size = Math.min(size, 1000);
        size = Math.max(size, 1);
        page = Math.max(page, 1);
        int start = (page - 1) * size;
        query.setFirstResult(start);
        query.setMaxResults(size);
        nativeQuery.setResultTransformer(Transformers.aliasToBean(dto));
        return nativeQuery.getResultList();
    }

    @Override
    public <T> List<T> listEntityPageBySql(Class<T> entityClass, String sql, int page, int size, Object... paramlist) {
        Query<T> query = getSession().createNativeQuery(sql);
        NativeQueryImpl<T> nativeQuery = (NativeQueryImpl<T>) query;
        setParameters(nativeQuery, paramlist);
        nativeQuery.addEntity(entityClass);
        size = Math.min(size, 1000);
        size = Math.max(size, 1);
        page = Math.max(page, 1);
        int start = (page - 1) * size;
        query.setFirstResult(start);
        query.setMaxResults(size);
        return nativeQuery.getResultList();
    }

    @Override
    public <T> List<T> listByProperty(Class<T> entityClass, String propertyName, Object value) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(builder.equal(root.get(propertyName), value));
        Query<T> q = getSession().createQuery(query);
        return q.getResultList();
    }

    @Override
    public <T> List<T> listByHql(final String hql, final Object... paramlist) {
        Query<T> query = getSession().createQuery(hql);
        setParameters(query, paramlist);
        return query.getResultList();
    }

    @Override
    public <T> List<T> listPageByHql(final String hql, int page, int size, final Object... paramlist) {
        Query<T> query = getSession().createQuery(hql);
        setParameters(query, paramlist);
        size = Math.min(size, 1000);
        size = Math.max(size, 1);
        page = Math.max(page, 1);
        int start = (page - 1) * size;
        query.setFirstResult(start);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public List<Map<Object, Object>> listPageBySql(final String sql, int page, int size, final Object... paramlist) {
        Query query = getSession().createNativeQuery(sql);
        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        setParameters(nativeQuery, paramlist);
        size = Math.min(size, 1000);
        size = Math.max(size, 1);
        page = Math.max(page, 1);
        int start = (page - 1) * size;
        nativeQuery.setFirstResult(start);
        nativeQuery.setMaxResults(size);
        nativeQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return nativeQuery.getResultList();
    }

    @Override
    public int executeUpdateWithHql(final String hql, final Object... paramlist) {
        Query query = getSession().createQuery(hql);
        setParameters(query, paramlist);
        Object result = query.executeUpdate();
        getSession().flush();
        return (Integer) result;
    }

    @Override
    public int executeUpdateWithSql(String sql, Object... paramlist) {
        Query query = getSession().createNativeQuery(sql);
        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        setParameters(nativeQuery, paramlist);
        int i = nativeQuery.executeUpdate();
        getSession().flush();
        return i;
    }

    @Override
    public DatabaseMetaData getMeta() throws SQLException {
        SessionImplementor sessionImp = (SessionImplementor) entityManager.getDelegate();
        return sessionImp.connection().getMetaData();
    }

    @Override
    public boolean isTableExist(String tableName) throws SQLException {
        DatabaseMetaData meta = getMeta();
        if (null != meta) {
            ResultSet rs;
            String[] types = {"TABLE"};
            rs = meta.getTables(null, null, "%", types);
            while (rs.next()) {
                String ttt = rs.getString("TABLE_NAME");
                log.error("拿到的:{},对比的:{}", ttt, tableName);
                if (tableName.equals(ttt)) {
                    return true;
                }
            }
        }
        return false;
    }


    //////////

    protected void setParameters(Query query, Object[] paramlist) {
        if (null != paramlist && paramlist.length > 0) {
            for (int i = 0; i < paramlist.length; i++) {
                if (paramlist[i] instanceof Date) {
                    query.setParameter(i, paramlist[i], TemporalType.TIMESTAMP);
                } else {
                    query.setParameter(i, paramlist[i]);
                }
            }
        }
    }

}
