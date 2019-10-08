package info.getbus.servebus.store;

import lombok.Getter;

public class NoSuchStoreException extends RuntimeException {
    @Getter
    private long id;
    public NoSuchStoreException(long id) {
        super("Store " + id + "not found");
        this.id = id;
    }
}
