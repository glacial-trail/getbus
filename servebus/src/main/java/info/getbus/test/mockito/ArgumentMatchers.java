package info.getbus.test.mockito;

import info.getbus.test.mockito.matchers.EqualsUsingReflection;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.Primitives;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

//stolen from mockito
public class ArgumentMatchers {
    /**
     * Object argument that is equal to the given value.
     * Recursively use reflections to compare object.
     */
    public static <T> T req(T value) {
        reportMatcher(new EqualsUsingReflection(value));
        if (value == null)
            return null;
        return (T) Primitives.defaultValue(value.getClass());
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
