package info.getbus.servebus.repository;

import info.getbus.servebus.dao.Mapper;
import info.getbus.servebus.model.Entity;

//TODO review repository
public abstract class AbstractRepository<T, E extends Entity<T>> implements Repository<T, E> {

    protected abstract Mapper<T, E> mapper();

    public E get(T id) {
        return mapper().selectById(id);
    }

    public E save(E entity) {
        if (entity.getId() == null) {
            mapper().insert(entity);
        } else {
            mapper().update(entity);
        }
        return entity;
    }

    public void delete(E entity) {
        mapper().delete(entity.getId());
    }

}
