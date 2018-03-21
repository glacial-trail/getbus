package info.getbus.utils.collect;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;

public class DoubleFor<T> {
    private Iterable<T> uno;
    private Iterable<T> dos;

    public DoubleFor(Iterable<T> uno, Iterable<T> dos) {
        this.uno = uno;
        this.dos = dos;
    }

    public void iterate(BiConsumer<? super T, ? super T> action) {
        Objects.requireNonNull(action);
        Iterator<T> itUno = uno.iterator();
        Iterator<T> itDos = dos.iterator();
        while (itUno.hasNext()) {
            if (!itDos.hasNext()) {
                throw new IllegalStateException("The size of iterables are different. Uno bigger");
            }
            T elementOfUno = itUno.next();
            T elementOfDos = itDos.next();
            action.accept(elementOfUno, elementOfDos);
        }
        if (itDos.hasNext()) {
            throw new IllegalStateException("The size of iterables are different. Dos bigger");
        }
    }
}