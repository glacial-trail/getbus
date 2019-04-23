package info.getbus.servebus.store.persistence;

import info.getbus.servebus.store.Store;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends CrudRepository<Store, Long> {
    Optional<Store> findStoreByCarrierId(long carrierId);
}
