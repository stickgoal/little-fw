package me.maiz.app.little.orm.dao;

import java.util.List;

public class UniversalDAO<E,PK> implements DAO<E,PK> {

    private Session session;

    @Override
    public int save(E e) {

        return 0;
    }

    @Override
    public int delete(PK id) {
        return 0;
    }

    @Override
    public E modify(E e) {
        return null;
    }

    @Override
    public E findById(PK id) {
        return null;
    }

    @Override
    public E findOne(E e) {
        return null;
    }

    @Override
    public List<E> find(String sql, Object... params) {
        return null;
    }

    @Override
    public PageResult<E> findPage(Page page, String sql, Object... params) {
        return null;
    }
}
