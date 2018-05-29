package info.getbus.test.harmcrest;

import info.getbus.test.harmcrest.object.IsEqualsUsingReflection;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public final class Matchers {
    private Matchers(){}

    public static <E> Matcher<java.util.Collection<? extends E>> hasSizeOf(Collection c) {
        return hasSize(c.size());
    }

    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(Iterable<T> items) {
        List<Matcher<? super T>> matchers = new ArrayList<>();
        for (T item : items) {
            matchers.add(equalTo(item));
        }
        return new IsIterableContainingInAnyOrder<>(matchers);
    }

    public static <T> Matcher<T> recursivelyEqualsTo(T obj) {
        return IsEqualsUsingReflection.recursivelyEqualsTo(obj);
    }
}
