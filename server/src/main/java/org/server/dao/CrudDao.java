package org.server.dao;

public interface CrudDao<ID, T> extends Dao<T> {

    void create(T obj);

    void update(T obj);

    void delete(ID id);
}
