package info.getbus.servebus.service.transporter;

import info.getbus.servebus.model.security.User;

public interface TransporterService {
    Long createBaseTransporterInfrastructure();
    void linkUserToArea(Long areaId, User user, String role);
}
