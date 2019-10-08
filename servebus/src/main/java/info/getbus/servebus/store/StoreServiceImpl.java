package info.getbus.servebus.store;

import info.getbus.servebus.service.security.SecurityHelper;
import info.getbus.servebus.store.persistence.StorePaymentDetailsBankUARepository;
import info.getbus.servebus.store.persistence.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final StorePaymentDetailsBankUARepository paymentDetailsRepository;
    private final SecurityHelper securityHelper;

//    @CarrierSecurity - check store belongs to carrier
    @Override
    public Store get(long id) {
        Optional<Store> store = storeRepository.findById(id);
        if (!store.isPresent()) {
            throw new NoSuchStoreException(id);
        }
        return store.get();
    }

    @Nullable
    @Override
    public Store get() {
        Long carrierId = securityHelper.getCurrentUser().getTransporterAreaId();
        return storeRepository.findStoreByCarrierId(carrierId).orElse(null);
    }

    @Override
    public StorePaymentDetailsBankUA getPaymentDetailsBankUA(long storeId) {
            return resultOrThrow(() -> paymentDetailsRepository.findById(storeId));
    }

    private <T> T resultOrThrow(Supplier<Optional<T>> supplier) {
        Optional<T> result = supplier.get();
        if (!result.isPresent()) {
            throw new NoSuchStoreException(0);
        }
        return result.get();
    }

    @Transactional
    @Override
    public void saveStore(Store store) {
        storeRepository.save(store);
    }
}
