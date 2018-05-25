package info.getbus.utils.collect;

import java.util.Iterator;
import java.util.List;

public class StrictBiterable<T> implements Biterable<T> {
    private final List<T> uno;
    private final List<T> dos;

    public StrictBiterable(List<T> uno, List<T> dos) {
        if (uno.size() != dos.size()) {
            throw new IllegalArgumentException();
        }
        this.uno = uno;
        this.dos = dos;
    }

    private class Biterator implements Iterator<Pair<T>> {
        private Iterator<T> ut;
        private Iterator<T> dt;

        public Biterator() {
            ut = uno.iterator();
            dt = dos.iterator();
        }


        @Override
        public boolean hasNext() {
            return ut.hasNext();
        }

        @Override
        public Pair<T> next() {
            T u = ut.next();
            T d = dt.next();
            return new Pair<T>() {
                @Override
                public T uno() {
                    return u;
                }

                @Override
                public T dos() {
                    return d;
                }
            };
        }
    }

    @Override
    public Iterator<Pair<T>> iterator() {
        return new Biterator();
    }
}
