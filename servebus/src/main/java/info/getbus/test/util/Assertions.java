package info.getbus.test.util;


import lombok.SneakyThrows;

import java.beans.PropertyDescriptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class Assertions {
    private Assertions(){}

    //TODO implement hamcrest matcher
    @SneakyThrows
    public static void assertThatObjectsAreEqualUsingFields(Object oAct, Object oExp, String... fields) {
        for (String field : fields) {
            Object actual = new PropertyDescriptor(field, oAct.getClass()).getReadMethod().invoke(oAct);
            Object expected = new PropertyDescriptor(field, oExp.getClass()).getReadMethod().invoke(oExp);
            assertNotNull(actual);
            if (expected instanceof Comparable) {
                //noinspection unchecked
                assertEquals(0, ((Comparable)actual).compareTo(expected), field);
            } else {
                assertEquals(expected, actual, field);
            }
        }
    }
}