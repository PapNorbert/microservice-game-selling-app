package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.BaseEntity;

public interface Repository<T extends BaseEntity, I> {
    T saveAndFlush(T entity);

    T getById(I id);

    // returns the number of updated entities
    Integer update(I id, T entity);

    void delete(T entity);

    void deleteById(Long id);

    void deleteById(I id);
}
