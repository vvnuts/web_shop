package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.services.interfaces.CrudService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCrudService<E, K> implements CrudService<E, K> {
    abstract JpaRepository<E, K> getRepository();

    @Override
    public void create(E entity) {
        getRepository().save(entity);
    }

    @Override
    public E findById(K id) {
        return getRepository().findById(id).orElse(null);
    }

    @Override
    public List<E> findAll() {
        return new ArrayList<>(getRepository().findAll());
    }

    @Override
    public void update(E entity) {
        getRepository().save(entity);
    }

    @Override
    public void delete(E entity) {
        getRepository().delete(entity);
    }
}
