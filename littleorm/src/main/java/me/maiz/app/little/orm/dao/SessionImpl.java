package me.maiz.app.little.orm.dao;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SessionImpl implements Session {

    private SessionFactory sessionFactory;

    SessionImpl(SessionFactory sessionFactory){

        this.sessionFactory = sessionFactory;
    }

    @Override
    public int save(Object instance) {

        String entityName = instance.getClass().getSimpleName();

        final Mapping mapping = sessionFactory.metaMap.get(entityName);
        if(mapping==null){
            throw new EntityNotManagedException(entityName+"未被管理");
        }

        try {
            //将对象转换为map形式的信息map
            Map<String, String> beanValues = BeanUtils.describe(instance);

            final String insertSql = SqlGenerator.insert(mapping);

            Object[] params = getParams(mapping.getColumnMappings(),beanValues);

            return  new DBHelper().add(insertSql,params);


        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
           throw new PersistException("持久化失败",e);
        }
    }

    private Object[] getParams(List<ColumnMapping> columnMappings, Map<String, String> beanValues) {
        List params = new ArrayList();
        for (ColumnMapping c : columnMappings) {
            params.add(beanValues.get(c.getProperty()));
        }
        return params.toArray();
    }

    private List<String> transform(Set<String> strings) {
        List<String> columns = new ArrayList<>();
        for (String prop : strings) {
            columns.add(StringUtil.camelCaseToUnderScore(prop));
        }
        return columns;
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
    public <T> T get(Class<T> entityType, Serializable id) {
        return null;
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
