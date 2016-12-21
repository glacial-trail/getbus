package info.getbus.servebus.service.validation;


public class UniqueValidationException extends RuntimeException {

    private boolean email;
    private boolean phone;

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }
}
