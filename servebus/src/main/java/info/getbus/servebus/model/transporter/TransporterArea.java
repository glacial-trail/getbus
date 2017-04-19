package info.getbus.servebus.model.transporter;


import info.getbus.servebus.model.AbstractEntity;

public class TransporterArea extends AbstractEntity<Long> {
    private String adminName;
    private String domain;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
