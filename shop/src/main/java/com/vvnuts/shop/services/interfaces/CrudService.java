package com.vvnuts.shop.services.interfaces;

import java.util.List;

public interface CrudService<E,D,K> {
    void create(D dtoEntity);
    E findById(K id);
    List<E> findAll();
    void update(D dtoEntity, K id);
    void delete(E entity);
}
