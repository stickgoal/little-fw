package me.maiz.app.little.orm.dao;

import java.util.List;

/**
 * DAO 接口
 *
 * @param <E>  实体类型
 * @param <PK> 主键类型
 */
public interface DAO<E, PK> {

    int save(E e);

    int delete(PK id);

    E modify(E e);

    E findById(PK id);

    E findOne(E e);

    /**
     * sql映射与查询，preparedStatement风格
     * @param sql
     * @param params
     * @return
     */
    List<E> find(String sql,Object... params);

    /**
     * sql映射与分页查询，preparedStatement风格
     * @param page
     * @param sql
     * @param params
     * @return
     */
    PageResult<E> findPage(Page page,String sql,Object... params);

}
