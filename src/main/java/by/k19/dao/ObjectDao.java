package by.k19.dao;

import java.util.List;

public interface ObjectDao<T> {
    T findById(long id);

    void save(T t);

    void update(T t);

    void delete(T t);

    List<T> findAll();
}
