package info.getbus.servebus.store.persistence;

import info.getbus.servebus.persistence.datamappers.transporter.TransporterAreaMapper;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterMapper;
import info.getbus.servebus.service.transporter.TransporterService;
import info.getbus.servebus.service.transporter.TransporterServiceImpl;
import info.getbus.servebus.store.Store;
import info.getbus.servebus.store.StorePaymentDetailsBankUA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;


@ExtendWith(SpringExtension.class)

@ContextConfiguration( classes = {StoreAwareJPATest.class})
@Configuration
@ImportResource("classpath:persistence-config.xml")
@ComponentScan("info.getbus.servebus.store.persistence")
@Transactional
@Rollback
class StoreAwareJPATest {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StorePaymentDetailsBankUARepository storePaymentDetailsRepository;


    @Bean
    public TransporterService transporterService(
            @Autowired TransporterAreaMapper transporterAreaMapper,
            @Autowired TransporterMapper transporterMapper
    ) {
        return new TransporterServiceImpl(transporterAreaMapper, transporterMapper);
    }

    @Autowired
    protected TransporterService transporterService;


    private Long transporterAreaId;
    private String domain = "dddommmaaa3";

    @BeforeEach
    void setUp() {
        transporterAreaId = transporterService.createInitialTransporterInfrastructure();
    }

    @Test
    void save_store() {
        Store store = makeStore();
        storeRepository.save(store);

        Optional<Store> storeSaved = storeRepository.findById(store.getId());
        assertThat(storeSaved.isPresent(), is(true));
        assertThat(storeSaved.get().getDomain(), is(domain));
    }

    @Test
    void findStoreByCarrierId() {
        Store store = makeStore();
        storeRepository.save(store);

        Optional<Store> storeSaved = storeRepository.findStoreByCarrierId(transporterAreaId);
        assertThat(storeSaved.isPresent(), is(true));
        assertThat(storeSaved.get().getDomain(), is(domain));
    }

    @Test
    void save_paymentDetails() {
        Store store = makeStore();
        storeRepository.save(store);

        StorePaymentDetailsBankUA paymentDetails = makePaymentDetails(store);
        storePaymentDetailsRepository.save(paymentDetails);

        Optional<StorePaymentDetailsBankUA> paymentDetailsSaved = storePaymentDetailsRepository.findById(paymentDetails.getId());

        assertThat(paymentDetailsSaved.isPresent(), is(true));
        assertReflectionEquals(paymentDetails, paymentDetailsSaved.get());
    }

    @Test
    void save_store_withPaymentDetails() {
        Store store = makeStore();
        StorePaymentDetailsBankUA paymentDetails = makePaymentDetails(store);

        storeRepository.save(store);

        Optional<Store> storeSaved = storeRepository.findById(store.getId());
        assertThat(storeSaved.isPresent(), is(true));
        assertReflectionEquals(store, storeSaved.get());
        assertReflectionEquals(paymentDetails, storeSaved.get().getPaymentDetails());
    }

    private Store makeStore() {
        Store s = new Store();
        s.setDomain(domain);
        s.setCarrierId(transporterAreaId);
        return s;
    }

    private StorePaymentDetailsBankUA makePaymentDetails(Store s) {
        StorePaymentDetailsBankUA pd = new StorePaymentDetailsBankUA();
        s.setPaymentDetails(pd);
        pd.setStore(s);
        pd.setAccount("000000000000111");
        pd.setMfo("000000000000112");
        pd.setOkpo("000000000000113");
        pd.setPhone("000000000000114");
        return pd;
    }
}