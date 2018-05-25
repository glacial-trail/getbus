package info.getbus.servebus.topology.persistence;

import info.getbus.servebus.topology.StopPlace;
import info.getbus.servebus.topology.persistence.mappers.StopPlaceMapper;
import info.getbus.test.mockito.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StopPlaceBatisRepositoryTest {

    @Mock
    private StopPlaceMapper mapper;

    @InjectMocks
    private StopPlaceBatisRepository repo;

    @Test
    void ensureSaved_new() {
        Long aid = 1L;
        StopPlace neW = new StopPlace();
        neW.setAddress(aid);
        when(mapper.selectByAddress(eq(aid))).thenReturn(null);
        StopPlace saved = repo.ensureSaved(neW);
        verify(mapper).selectByAddress(eq(aid));
        verify(mapper).insert(eq(neW));
        verifyNoMoreInteractions(mapper);
        assertThat(neW, is(saved));
    }

    @Test
    void ensureSaved_exist() {
        StopPlace saved = new StopPlace();
        saved.setAddress(1L);
        StopPlace neW = new StopPlace();
        neW.setAddress(2L);
        when(mapper.selectByAddress(eq(2L))).thenReturn(saved);
        StopPlace expSaved = repo.ensureSaved(neW);
        verify(mapper).selectByAddress(eq(2L));
        verifyNoMoreInteractions(mapper);
        assertThat(expSaved, is(saved));
        assertThat(expSaved, is(not(neW)));
    }
}