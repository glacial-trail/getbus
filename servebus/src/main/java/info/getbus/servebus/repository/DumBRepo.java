package info.getbus.servebus.repository;

import info.getbus.servebus.dao.BMapper;
import info.getbus.servebus.model.tmp.B;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class DumBRepo extends AbstractRepository<Long, B> implements BRepo {
    @Autowired
    private BMapper mapper;

    public Collection<B> getAll() {
        return mapper.selectAll();
    }

    @Override
    protected BMapper mapper() {
        return null;
    }
}
