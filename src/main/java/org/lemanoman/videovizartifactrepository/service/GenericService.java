package org.lemanoman.videovizartifactrepository.service;

import org.lemanoman.videovizartifactrepository.interfaces.MergeableModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenericService<T extends MergeableModel<T,ID>, ID> {
    JpaRepository<T, ID> repository;
    public GenericService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public T findById(ID id) {
        return repository.findById(id).orElse(null);
    }

    public T saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            return repository.save(entity);
        }
        T entityFromDb = repository.findById(entity.getId()).orElse(null);
        if (Objects.isNull(entityFromDb)) {
            return repository.save(entity);
        }
        entityFromDb.merge(entity);
        return repository.saveAndFlush(entityFromDb);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public long count() {
        return repository.count();
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteAll(Iterable<? extends T> entities) {
        repository.deleteAll(entities);
    }

    public List<T> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }
}
