package info.getbus.servebus.persistence;

public class LockedEntityException extends RuntimeException {
    private Long id;
    private String requestor;
    private String owner;

    public LockedEntityException(Long id, String requestor, String owner) {
        super(requestor + " tries to use entity{" + id + "} locked by " + owner);
        this.id = id;
        this.requestor = requestor;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getRequestor() {
        return requestor;
    }

    public String getOwner() {
        return owner;
    }
}
