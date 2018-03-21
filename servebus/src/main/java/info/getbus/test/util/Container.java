package info.getbus.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Container<K,O,V> {
    private Function<O, K> keyFunc;
    private Function<O, V> valFunc;
    private Map<K, V> entities = new HashMap<>();

    public Container(Function<O, K> keyFunc, Function<O, V> valFunc) {
        this.keyFunc = keyFunc;
        this.valFunc = valFunc;
    }

    public void put(O entity) {
        entities.put(keyFunc.apply(entity), valFunc.apply(entity));
    }
    public void put(K key, O entity) {
        entities.put(key, valFunc.apply(entity));
    }
    public V getFor(O entity) {
        //TODO harmcrest?
        assertTrue(entities.containsKey(keyFunc.apply(entity)));
        return entities.get(keyFunc.apply(entity));
    }
    public V getByKey(K key) {
        return entities.get(key);
    }
}
