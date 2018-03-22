package info.getbus.servebus.model.transporter;

import info.getbus.servebus.model.AbstractEntity;
import info.getbus.servebus.geo.address.Address;

public class Transporter extends AbstractEntity<Long> {
    private Address actualAddress;
    private Address registeredOffice;

    public Address getActualAddress() {
        return actualAddress;
    }

    public void setActualAddress(Address actualAddress) {
        this.actualAddress = actualAddress;
    }

    public Address getRegisteredOffice() {
        return registeredOffice;
    }

    public void setRegisteredOffice(Address registeredOffice) {
        this.registeredOffice = registeredOffice;
    }
}
