package info.getbus.servebus.validation.validator;

import info.getbus.servebus.dao.security.UserMapper;
import info.getbus.servebus.model.security.RegisterUserDTO;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.validation.constraints.UniqueUser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class JdbcUniquenessUserValidator  implements ConstraintValidator<UniqueUser, RegisterUserDTO> {
    @Autowired
    private UserMapper mapper;

    private String messageEmail;
    private String messagePhone;

    @Override
    public void initialize(UniqueUser constraintAnnotation) {
        messageEmail = constraintAnnotation.messageEmail();
        messagePhone = constraintAnnotation.messagePhone();
    }

    @Override
    public boolean isValid(RegisterUserDTO user, ConstraintValidatorContext context) {
        Collection<User> users = mapper.selectByUsernameOrPhone(user.getEmail(), user.getPhone());
        if (!users.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (User u : users) {
                if (user.getEmail().equals(u.getEmail())) {
                    context.buildConstraintViolationWithTemplate(messageEmail)
                            .addPropertyNode("email")
                            .addConstraintViolation();
                }
                if (user.getPhone().equals(u.getPhone())) {
                    context.buildConstraintViolationWithTemplate(messagePhone)
                            .addPropertyNode("phone")
                            .addConstraintViolation();
                }
            }
            return false;
        }
        return true;
    }
}