package me.maiz.app.little.orm.dao;

import lombok.extern.slf4j.Slf4j;
import me.maiz.app.little.orm.exceptions.EntityNotManagedException;
import me.maiz.app.little.orm.exceptions.PersistException;
import me.maiz.app.little.orm.meta.model.ColumnMapping;
import me.maiz.app.little.orm.meta.model.SqlGenerator;
import me.maiz.app.little.orm.meta.model.Mapping;
import me.maiz.app.little.orm.util.DBHelper;
import me.maiz.app.little.orm.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Slf4j
public class SessionImpl implements Session {

    private SessionFactory sessionFactory;

    private DBHelper dbHelper = new DBHelper();

    SessionImpl(SessionFactory sessionFactory){

        this.sessionFactory = sessionFactory;
    }

    @Override
    public int save(Object instance) {

        dbHelper = new DBHelper();

        String entityName = instance.getClass().getSimpleName();

        final Mapping mapping = getMapping(entityName);

        try {
            //将对象转换为map形式的信息map
            Map<String, String> beanValues = BeanUtils.describe(instance);

            final String insertSql = SqlGenerator.insert(mapping);

            Object[] params = getParams(mapping.getColumnMappings(),beanValues);

            return  dbHelper.add(insertSql,params);


        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
           throw new PersistException("持久化失败",e);
        }finally{
            dbHelper.cleanup();
        }
    }

    private Mapping getMapping(String entityName) {
        final Mapping mapping = sessionFactory.metaMap.get(entityName);
        if(mapping==null){
            throw new EntityNotManagedException(entityName+"未被管理");
        }
        return mapping;
    }

    private Object[] getParams(List<ColumnMapping> columnMappings, Map<String, String> beanValues) {
        List params = new ArrayList();
        for (ColumnMapping c : columnMappings) {
            params.add(beanValues.get(c.getProperty()));
        }
        return params.toArray();
    }

    @Override
    public int delete(Object instance) {
        return 0;
    }

    @Override
    public int update(Object instance) {
        return 0;
    }

    @Override
    public void beginTransaction() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }



    @Override
    public <T> T get(final Class<T> entityType, Object id) {

        final Mapping mapping = getMapping(entityType.getSimpleName());
        final DBHelper.Transformer<Object> transformer = new DBHelper.Transformer<Object>() {

            @Override
            public Object extract(ResultSet rs) throws SQLException {
                Object instance = null;
                try {
                    instance = entityType.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("实例化异常", e);
                }
                final ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    final String columnName = resultSetMetaData.getColumnName(i);
                    try {
                        //设置值，约定为 下划线风格 => 驼峰式风格
                        BeanUtils.setProperty(instance, StringUtil.underScoreToCamelCase(columnName), rs.getObject(i));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("设置"+columnName+"值出错",e);
                    }
                }

                return instance;
            }
        };

        final List<Object> resultObj = dbHelper.query(SqlGenerator.byId(mapping), transformer, id);

        return resultObj!=null?(T)resultObj.get(0):null;
    }

    @Override
    public <T> List<T> createQuery(Class<T> entityType, String sql, Object... param) {



        return null;
    }

    @Override
    public <T> PageResult<T> createPageQuery(Class<T> entityType, String sql, Page page, Object... param) {
        return null;
    }
}
