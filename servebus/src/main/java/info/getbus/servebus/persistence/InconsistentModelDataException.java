package info.getbus.servebus.persistence;

public class InconsistentModelDataException extends RuntimeException {
    private Class<?> clazz;
    private String description;

    public InconsistentModelDataException(Class<?> clazz, String description) {
        super("Inconsistency between expected model of " + clazz.getSimpleName() + " and data model. " + description);
        this.clazz = clazz;
        this.description = description;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getDescription() {
        return description;
    }
}
