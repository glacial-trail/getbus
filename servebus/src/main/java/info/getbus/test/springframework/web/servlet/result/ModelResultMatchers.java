package info.getbus.test.springframework.web.servlet.result;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;

//stolen from spring
public class ModelResultMatchers {
    /**
     * Assert the given model attribute field(s) have no errors.
     */
    public static ResultMatcher attributeHasNoFieldErrors(final String name, final String... fieldNames) {
        return mvcResult -> {
            ModelAndView mav = getModelAndView(mvcResult);
            BindingResult result = getBindingResult(mav, name);
            for (final String fieldName : fieldNames) {
                boolean hasFieldErrors = result.hasFieldErrors(fieldName);
                assertTrue("Unexpected errors fo field '" + fieldName + "' of attribute '" + name + "'", !hasFieldErrors);
            }
        };
    }

    private static ModelAndView getModelAndView(MvcResult mvcResult) {
        ModelAndView mav = mvcResult.getModelAndView();
        if (mav == null) {
            fail("No ModelAndView found");
        }
        return mav;
    }

    private static BindingResult getBindingResult(ModelAndView mav, String name) {
        BindingResult result = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + name);
        if (result == null) {
            fail("No BindingResult for attribute: " + name);
        }
        return result;
    }
}