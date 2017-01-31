package info.getbus.servebus.validation.constraints;

import info.getbus.servebus.validation.validator.JdbcUniquenessUserValidator;

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
@Constraint(validatedBy = JdbcUniquenessUserValidator.class)
@Documented
public @interface UniqueUser {
    String message() default "info.getbus.servebus.validation.constraints.UniqueUser";
    String messageEmail() default "info.getbus.servebus.validation.constraints.UniqueUser.email";
    String messagePhone() default "info.getbus.servebus.validation.constraints.UniqueUser.phone";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
