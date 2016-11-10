package info.getbus.servebus.repository;

import info.getbus.servebus.dao.BMapper;
import info.getbus.servebus.model.tmp.B;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class DumBRepo implements BRepo {
    @Autowired
    BMapper bMapper;

    public Collection<B> getAll() {
        return bMapper.selectAll();
    }
}
