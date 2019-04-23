package info.getbus.servebus.store.persistence;

import info.getbus.servebus.store.StorePaymentDetailsBankUA;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePaymentDetailsBankUARepository extends CrudRepository<StorePaymentDetailsBankUA, Long> {
}
