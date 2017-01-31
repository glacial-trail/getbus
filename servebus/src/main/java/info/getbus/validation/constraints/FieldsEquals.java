package info.getbus.validation.constraints;

import info.getbus.validation.validator.FieldsEqualsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldsEqualsValidator.class)
@Documented
public @interface FieldsEquals {
    String message() default "info.getbus.validation.constraints.FieldsEquals";
    String[] value();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
