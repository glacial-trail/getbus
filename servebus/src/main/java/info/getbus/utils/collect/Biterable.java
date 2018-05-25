package info.getbus.utils.collect;

public interface Biterable<T> extends Iterable<Biterable.Pair<T>> {
    public interface Pair<T> {
        T uno();
        T dos();
    }
}
