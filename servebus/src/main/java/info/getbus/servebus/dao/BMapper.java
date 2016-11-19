package info.getbus.servebus.dao;

import info.getbus.servebus.model.tmp.B;

import java.util.Collection;

public interface BMapper extends Mapper<Long, B> {

    Collection<B> selectAll();
}
