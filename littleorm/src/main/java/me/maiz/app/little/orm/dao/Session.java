package me.maiz.app.little.orm.dao;

import java.io.Serializable;
import java.util.List;

public interface Session {


    /**
     * 保存
     * @param instance 需要保存的实例
     * @return
     */
    public int save(Object instance);

    /**
     * 删除
     * @param instance 需要被删除的实例
     * @return
     */
    public int delete(Object instance);

    /**
     * 根据ID更新
     * @param instance 需要被更新的实例
     * @return
     */
    public int update(Object instance);

    /**
     * 根据ID查询
     * @param entityType
     * @param id
     * @param <T>
     * @return
     */
    <T> T get(Class<T> entityType, Serializable id);

    /**
     * 根据语句执行查询
     * @param entityType
     * @param sql
     * @param param
     * @param <T>
     * @return
     */
    <T> List<T> createQuery(Class<T> entityType,String sql,Object... param);

 /**
     * 根据语句执行查询
     * @param entityType
     * @param sql
     * @param param
     * @param <T>
     * @return
     */
    <T> PageResult<T> createPageQuery(Class<T> entityType,String sql,Page page,Object... param);

}
