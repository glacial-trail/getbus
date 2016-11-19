package info.getbus.servebus.repository;

import info.getbus.servebus.model.tmp.B;

import java.util.Collection;

public interface BRepo extends Repository<Long, B> {
    Collection<B> getAll();
}
