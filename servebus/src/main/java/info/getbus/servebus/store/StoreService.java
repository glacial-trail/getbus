package info.getbus.servebus.store;

public interface StoreService {
    Store get(long id);
    Store get();
    StorePaymentDetailsBankUA getPaymentDetailsBankUA(long storeId);
    void saveStore(Store store);
}
