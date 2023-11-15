package com.vvnuts.shop.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.vvnuts.shop.services.interfaces.CrudService;
import com.vvnuts.shop.utils.Views;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class AbstractCrudController<E, D, K> {
    abstract CrudService<E, D, K> getService();

    @PostMapping()
    @JsonView(Views.Low.class)
    public ResponseEntity<HttpStatus> create(@RequestBody D dtoEntity){
        getService().create(dtoEntity);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    @JsonView(Views.Low.class)
    public ResponseEntity<List<E>> findAll(){
        List<E> entities = getService().findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Low.class)
    public ResponseEntity<E> findOne(@PathVariable K id) {
        E entity = getService().findById(id);
        if(entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Low.class)
    public ResponseEntity<HttpStatus> update(@PathVariable K id,
            @RequestBody D dtoEntity) {
        getService().update(dtoEntity, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @JsonView(Views.Low.class)
    public ResponseEntity<HttpStatus> delete(@PathVariable K id) {
        E entity = getService().findById(id);
        getService().delete(entity);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
