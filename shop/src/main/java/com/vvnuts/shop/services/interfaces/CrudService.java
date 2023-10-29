package com.vvnuts.shop.services.interfaces;

import java.util.List;

public interface CrudService<E,K> {
    void create(E entity);
    E findById(K id);
    List<E> findAll();
    void update(E entity);
    void delete(E entity);
}
