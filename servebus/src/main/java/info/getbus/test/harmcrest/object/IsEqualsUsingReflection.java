package info.getbus.test.harmcrest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

// TODO see org.hamcrest.core.IsEqual
// TODO make separate Equality matcher used by all matchers. Tests.
public class IsEqualsUsingReflection<T> extends TypeSafeMatcher<T> {
    private final T actual;

    private final boolean testTransients = true;
//  private final Class<?> reflectUpToClass = null;
    private final boolean testRecursive;
//  final String... excludeFields
//  final String... includeFields

    public IsEqualsUsingReflection(T actual, boolean testRecursive) {
        this.actual = actual;
        this.testRecursive = testRecursive;
    }

    @Override
    public boolean matchesSafely(T expected) {
        return EqualsBuilder.reflectionEquals(actual, expected, testTransients, null, testRecursive);
    }

    @Override
    public void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendText(reflectionToString(item));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(reflectionToString(actual));
    }

    private String reflectionToString(T obj) {
        return ToStringBuilder.reflectionToString(obj,
                ToStringStyle.NO_CLASS_NAME_STYLE,
                testTransients
        );
    }

    /**
     * Creates a matcher that matches when the examined object is logically equal to the specified
     * <code>operand</code>, as determined by calling the {@link java.lang.Object#equals} method on
     * the <b>examined</b> object.
     */
//  public static <T> Matcher<T> recursivelyEqualsUsingReflectionTo(T obj) {
    public static <T> Matcher<T> recursivelyEqualsTo(T obj) {
        return new IsEqualsUsingReflection<>(obj, true);
    }
/*
        public static <T> Matcher<T> equalsNonRecursivelyWithReflectionTo(T obj) {
            return new IsEqualsUsingReflection<>(obj, false);
        }
*/
}
