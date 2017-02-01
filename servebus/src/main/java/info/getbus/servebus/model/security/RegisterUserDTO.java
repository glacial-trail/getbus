package info.getbus.servebus.model.security;

import info.getbus.servebus.validation.constraints.UniqueUser;
import info.getbus.validation.constraints.FieldsEquals;
import org.hibernate.validator.constraints.NotEmpty;
import info.getbus.servebus.model.security.RegisterUserDTO.SecondStep;
import info.getbus.servebus.model.security.RegisterUserDTO.FourthStep;


import javax.validation.GroupSequence;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@UniqueUser(groups = SecondStep.class,
    messageEmail = "register.partner.enter_email.error.exist",
    messagePhone = "register.partner.enter_phone.error.exist")
@FieldsEquals(value = {"repassword", "password"},
        groups = FourthStep.class,
        message = "register.partner.password.error.notmatch")
public class RegisterUserDTO {
@NotEmpty(groups = ValidateAnyway.class,
        message = "register.partner.enter_firstname.error")
private String firstname;
@NotEmpty(groups = ValidateAnyway.class,
        message = "register.partner.enter_lastname.error")
private String lastname;
@Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
        groups = FirstStep.class,
        message = "register.partner.enter_email.error")
private String email;
@Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$",
        groups = FirstStep.class,
        message = "register.partner.enter_phone.error")
private String phone;
@Size(min = 6, groups = ThirdStep.class,
    message = "register.partner.password.error.invalid")
private String   password;
private String repassword;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public interface ValidateAnyway {}
    public interface FirstStep {}
    public interface SecondStep {}
    public interface ThirdStep {}
    public interface FourthStep {}
    @GroupSequence({FirstStep.class, SecondStep.class, ThirdStep.class, FourthStep.class})
    public interface Sequence {}
}
