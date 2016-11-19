package info.getbus.servebus.repository;

import info.getbus.servebus.model.Entity;

public interface Repository<T, E extends Entity<T>>  {
        E get(T id);
        E save(E entity);
        void delete(E entity);
}
