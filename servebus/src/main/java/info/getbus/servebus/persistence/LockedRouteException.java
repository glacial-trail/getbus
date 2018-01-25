package info.getbus.servebus.persistence;

public class LockedRouteException extends RuntimeException {
    private Long id;
    private String requester;
    private String owner;

    public LockedRouteException(Long id, String requester, String owner) {
        super(requester + " acquires route " + id + " locked by " + owner);
        this.id = id;
        this.requester = requester;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getRequester() {
        return requester;
    }

    public String getOwner() {
        return owner;
    }
}
