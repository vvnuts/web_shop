package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.services.interfaces.CrudService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCrudService<E, D, K> implements CrudService<E, D, K>{
    abstract JpaRepository<E, K> getRepository();
    abstract E transferToUpdateEntity(D dto, E updateEntity);
    abstract E transferToCreateEntity(D dto);

    @Override
    public void create(D dtoEntity) {
        E entity = transferToCreateEntity(dtoEntity);
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
    public void update(D dtoEntity, K id) {
        E updateEntity = findById(id);
        E entity = transferToUpdateEntity(dtoEntity, updateEntity);
        getRepository().save(entity);
    }

    @Override
    public void delete(E entity) {
        getRepository().delete(entity);
    }
}
