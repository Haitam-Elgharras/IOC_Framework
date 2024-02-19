package org.example.dao;

import java.util.List;

public interface Dao<T,U> {
    void create(T t);
    T findById(U id);
    List<T> getAll();
    void update(T t);
    void delete(U id);
}
