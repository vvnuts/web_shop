package com.vvnuts.shop.controllers;

import com.vvnuts.shop.services.interfaces.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class AbstractCrudController<E, K> {
    abstract CrudService<E, K> getService();

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody E entity){
        getService().create(entity);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<E>> findAll(){
        List<E> entities = getService().findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<E> findOne(@PathVariable K id) {
        E entity = getService().findById(id);
        if(entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> update(@RequestBody E entity) {
        getService().update(entity);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable K id) {
        E entity = getService().findById(id);
        getService().delete(entity);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
