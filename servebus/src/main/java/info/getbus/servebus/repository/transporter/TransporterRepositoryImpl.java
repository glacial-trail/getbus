package info.getbus.servebus.repository.transporter;

import info.getbus.servebus.dao.transporter.TransporterMapper;
import info.getbus.servebus.model.transporter.Transporter;
import info.getbus.servebus.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransporterRepositoryImpl extends AbstractRepository<Long, Transporter> implements TransporterRepository {
    @Autowired
    private TransporterMapper mapper;


    @Override
    protected TransporterMapper mapper() {
        return mapper;
    }
}
