package info.getbus.test.mockito.matchers;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;
import org.mockito.internal.matchers.text.ValuePrinter;

import java.io.Serializable;

//stolen from mockito
//TODO see org.mockito.internal.matchers.Equality and org.mockito.internal.matchers.Equals
public class EqualsUsingReflection implements ArgumentMatcher<Object>, ContainsExtraTypeInfo, Serializable {

    private final Object wanted;

    public EqualsUsingReflection(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
//        return Equality.areEqual(this.wanted, actual);
        return EqualsBuilder.reflectionEquals(this.wanted, actual, true, null, true);

    }

    public String toString() {
        return describe(wanted);
    }

    private String describe(Object object) {
        return ValuePrinter.print(object);
    }

    protected final Object getWanted() {
        return wanted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }
        EqualsUsingReflection other = (EqualsUsingReflection) o;
        return this.wanted == null && other.wanted == null || this.wanted != null && this.wanted.equals(other.wanted);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public String toStringWithType() {
        return "("+ wanted.getClass().getSimpleName() +") " + describe(wanted);
    }

    public boolean typeMatches(Object target) {
        return wanted != null && target != null && target.getClass() == wanted.getClass();
    }
}
