package info.getbus.servebus.geo.address.persistence.mappers;

import info.getbus.servebus.geo.address.Address;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
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
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration( locations = {"classpath:persistence-config.xml"})
@Transactional
@Rollback
class AddressMapperTest {
    @Autowired
    private AddressMapper mapper;

    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .exclude(field().named("id").ofType(Long.class).inClass(Address.class).get())
            .stringLengthRange(5,10).build();

    @Test
    void insertPartiallySelect() {
        Address exp = random.nextObject(Address.class);
        exp.setCountryCode("UA");
        mapper.insert(exp);
        assertThat(exp.getId(), notNullValue());
        Address act = mapper.select(exp.getId());
        assertThat(act, nullValue());
    }

    @Test
    void insertSelect() {
        Address exp = random.nextObject(Address.class);
        exp.setCountryCode("UA");
        mapper.insert(exp);
        mapper.insertL10n(exp);
        assertThat(exp.getId(), notNullValue());
        Address act = mapper.select(exp.getId());
        assertThatAddressesAreEqual(exp, act);
    }

    @Test
    void selectSame() {
        Address exp = random.nextObject(Address.class);
        exp.setCountryCode("UA");
        mapper.insert(exp);
        mapper.insertL10n(exp);
        Address act = mapper.selectSame(exp);
        assertThatAddressesAreEqual(exp, act);
    }

    private void assertThatAddressesAreEqual(Address exp, Address act) {
        assertThatObjectsAreEqualUsingFields(act, exp,
                "id",
                "countryCode",
                "adminArea1",
                "city",
                "streetBuilding",
                "street",
                "building",
                "zip",
                "utcOffset"
        );
    }
}