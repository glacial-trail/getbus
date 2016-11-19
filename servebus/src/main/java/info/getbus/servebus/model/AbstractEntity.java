package info.getbus.servebus.model;

import info.getbus.servebus.model.Entity;

import javax.annotation.Nullable;

public abstract class AbstractEntity<T> implements Entity<T> {
    private T id;

    @Override
    @Nullable
    public T getId() {
        return id;
    }

    @Override
    public void setId(T id) {

    }
}
