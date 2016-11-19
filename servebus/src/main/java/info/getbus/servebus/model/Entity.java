package info.getbus.servebus.model;

import javax.annotation.Nullable;

public interface Entity<T>{
    @Nullable
    T getId();
    void setId(T id);
}
