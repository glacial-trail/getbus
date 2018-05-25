package info.getbus.servebus.topology.persistence.mappers;

import info.getbus.servebus.geo.address.Address;
import info.getbus.servebus.geo.address.persistence.mappers.AddressMapper;
import info.getbus.servebus.topology.StopPlace;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static info.getbus.test.util.Assertions.assertThatObjectsAreEqualUsingFields;
import static io.github.benas.randombeans.FieldDefinitionBuilder.field;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.junit.MatcherAssert.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration( locations = {"classpath:persistence-config.xml"})
@Transactional
@Rollback
class StopPlaceMapperTest {
    @Autowired
    private AddressMapper am;
    @Autowired
    private StopPlaceMapper mapper;


    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .exclude(field().named("id").ofType(Long.class).inClass(Address.class).get())
            .stringLengthRange(5,10).build();


    private Long addressId;

    @BeforeEach
    void setUp() {
        Address address = random.nextObject(Address.class);
        address.setCountryCode("UA");
        am.insert(address);
        am.insertL10n(address);
        addressId = address.getId();
    }

    StopPlace newPlace() {
        StopPlace place = new StopPlace();
        place.setName("name");
        place.setAddress(addressId);
        return place;
    }

    @Test
    void insertSelectByAddress() {
        StopPlace exp = newPlace();
        mapper.insert(exp);
        assertThat(exp.getId(), notNullValue());
        StopPlace act = mapper.selectByAddress(exp.getAddress());
//        assertThat(act, recursivelyEqualsTo(exp));// TODO fix strange string behavior
        assertThatObjectsAreEqualUsingFields(act, exp,
                "id",
                "name",
                "address"
        );
    }

}