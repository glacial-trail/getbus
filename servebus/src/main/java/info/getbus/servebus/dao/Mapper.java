package info.getbus.servebus.dao;

import info.getbus.servebus.model.Entity;

import javax.annotation.Nonnull;

public interface  Mapper<T, E extends Entity<T>> {
        E selectById(T id);
        void insert(E entity);
        void update(E entity);
        void delete(@Nonnull T entity);
}
