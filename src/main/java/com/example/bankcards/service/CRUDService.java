package com.example.bankcards.service;

import java.util.Collection;

public interface CRUDService<T> {

    void create(T item);

    T getById(Long id);

    Collection<T> getAll();

    void update(Long id, T item);

    void delete(Long id);
}
