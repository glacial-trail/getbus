package info.getbus.servebus.service.transporter;

import info.getbus.servebus.persistence.datamappers.transporter.TransporterAreaMapper;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterMapper;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.model.transporter.TransporterArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransporterServiceImpl implements TransporterService {
    private TransporterAreaMapper transporterAreaMapper;
    private TransporterMapper transporterMapper;

    public TransporterServiceImpl(
            @Autowired TransporterAreaMapper transporterAreaMapper,
            @Autowired TransporterMapper transporterMapper
    ) {
        this.transporterAreaMapper = transporterAreaMapper;
        this.transporterMapper = transporterMapper;
    }

    @Override
    public Long createBaseTransporterInfrastructure() {
        TransporterArea ta = new TransporterArea();
        transporterAreaMapper.insertEmpty(ta);
        transporterMapper.insertEmpty(ta.getId());
        return ta.getId();
    }

    @Override
    public void linkUserToArea(Long areaId, User user, String role) {
        transporterAreaMapper.insertUser2Transporter(areaId, user.getUsername(), role);
    }
}
