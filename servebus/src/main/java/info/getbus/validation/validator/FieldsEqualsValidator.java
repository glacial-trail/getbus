package info.getbus.validation.validator;

import info.getbus.validation.constraints.FieldsEquals;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

public class FieldsEqualsValidator implements ConstraintValidator<FieldsEquals, Object> {
    private String[] fieldNames;
    private String message;

    @Override
    public void initialize(FieldsEquals constraintAnnotation) {
        fieldNames = constraintAnnotation.value();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (fieldNames.length < 2 ) {
            return true;
        }
        String first = fieldNames[0];

        String firstValue;
        boolean valid;
        try {
            firstValue = BeanUtils.getProperty(obj, first);
            valid = firstValue == null ? allNulls(obj) : allEquals(firstValue, obj);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(first).addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }

    private boolean allNulls(Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return new CompareTemplate(obj) {
            @Override
            boolean eq(String curVal) {
                return null == curVal;
            }
        }.isEquals();
    }

    private boolean allEquals(final String firstVal, Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return new CompareTemplate(obj) {
            @Override
            boolean eq(String curVal) {
                return firstVal.equals(curVal);
            }
        }.isEquals();
    }

    private abstract class CompareTemplate {
        private Object obj;
        CompareTemplate(Object obj) {
            this.obj = obj;
        }
        boolean isEquals() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            for (int i = 1; i < fieldNames.length; i++) {
                String curVal = BeanUtils.getProperty(obj, fieldNames[i]);
                if (!eq(curVal)) {
                    return false;
                }
            }
            return true;
        }
        abstract boolean eq(String curVal);
    }
}
