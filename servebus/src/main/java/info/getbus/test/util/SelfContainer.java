package info.getbus.test.util;

import java.util.function.Function;

public class SelfContainer<K,O> extends Container<K,O,O> {
    public SelfContainer(Function<O, K> keyFunc) {
        super(keyFunc, o -> o);
    }
}