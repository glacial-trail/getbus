package info.getbus.servebus.geo.address.persistence;

import info.getbus.servebus.geo.address.Address;
import info.getbus.servebus.geo.address.persistence.mappers.AddressMapper;
import info.getbus.test.mockito.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressJdbcRepositoryTest {

    @Mock
    private AddressMapper mapper;

    @InjectMocks
    private AddressJdbcRepository repo;


    @Test
    void get() {
        repo.get(42L);
        verify(mapper).select(eq(42L));
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void ensureSaved_new() {
        when(mapper.selectSame(any())).thenReturn(null);
        Address neW = new Address();
        Address saved = repo.ensureSaved(neW);
        verify(mapper).selectSame(eq(neW));
        verify(mapper).insert(eq(neW));
        verify(mapper).insertL10n(eq(neW));
        verifyNoMoreInteractions(mapper);
        assertThat(neW, is(saved));
    }

    @Test
    void ensureSaved_exist() {
        Address saved = new Address();
        Address neW = new Address();
        when(mapper.selectSame(eq(neW))).thenReturn(saved);
        Address expSaved = repo.ensureSaved(neW);
        verify(mapper).selectSame(eq(neW));
        verifyNoMoreInteractions(mapper);
        assertThat(expSaved, is(saved));
        assertThat(expSaved, is(not(neW)));
    }
}