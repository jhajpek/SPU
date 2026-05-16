package hr.fer.zpr.infsus.spu.unit.service;

import hr.fer.zpr.infsus.spu.dto.DvoranaFormDto;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.service.impl.DvoranaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DvoranaServiceTests {

    @Mock
    private LokacijaRepository lokacijaRepository;

    @Mock
    private DvoranaRepository dvoranaRepository;

    @InjectMocks
    private DvoranaServiceImpl dvoranaServiceImpl;

    @Test
    public void DvoranaService_FindAllUnusedLokacije_ReturnLokacijaList() {
        Lokacija lokacija1 = new Lokacija();
        lokacija1.setLokacijaId(1L);

        Lokacija lokacija2 = new Lokacija();
        lokacija2.setLokacijaId(2L);

        Dvorana dvorana = new Dvorana();
        dvorana.setLokacija(lokacija1);

        when(dvoranaRepository.findAll()).thenReturn(List.of(dvorana));
        when(lokacijaRepository.findAll()).thenReturn(List.of(lokacija1, lokacija2));

        List<Lokacija> lokacije = dvoranaServiceImpl.findAllUnusedLokacije();

        Assertions.assertEquals(1, lokacije.size());
        Assertions.assertEquals(2L, lokacije.get(0).getLokacijaId());

        verify(lokacijaRepository, times(1)).findAll();
        verify(dvoranaRepository, times(1)).findAll();
    }

    @Test
    public void DvoranaService_SaveDvorana_ReturnSavedDvorana() {
        Long lokacijaId = 1L;

        Lokacija lokacija = new Lokacija();
        lokacija.setLokacijaId(lokacijaId);

        DvoranaFormDto dvoranaFormDto = new DvoranaFormDto();
        dvoranaFormDto.setNaziv("Arena");
        dvoranaFormDto.setLokacijaId(lokacijaId);

        when(lokacijaRepository.findById(lokacijaId)).thenReturn(Optional.of(lokacija));
        when(dvoranaRepository.existsByLokacija_LokacijaId(lokacijaId)).thenReturn(false);
        when(dvoranaRepository.save(any(Dvorana.class))).thenAnswer(i -> i.getArguments()[0]);

        Dvorana savedDvorana = dvoranaServiceImpl.save(dvoranaFormDto);

        Assertions.assertNotNull(savedDvorana);
        Assertions.assertEquals("Arena", savedDvorana.getNaziv());
        verify(lokacijaRepository, times(1)).findById(anyLong());
        verify(dvoranaRepository, times(1)).existsByLokacija_LokacijaId(anyLong());
        verify(dvoranaRepository, times(1)).save(any(Dvorana.class));
    }

    @Test
    public void DvoranaService_SaveDvoranaWhenLokacijaExists_ThrowsException() {
        Long lokacijaId = 1L;

        DvoranaFormDto dvoranaFormDto = new DvoranaFormDto();
        dvoranaFormDto.setLokacijaId(lokacijaId);

        when(dvoranaRepository.existsByLokacija_LokacijaId(lokacijaId)).thenReturn(true);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> dvoranaServiceImpl.save(dvoranaFormDto)
        );
        verify(dvoranaRepository, never()).save(any(Dvorana.class));
    }

    @Test
    public void DvoranaService_UpdateDvorana_ReturnUpdatedDvorana() {
        Long dvoranaId = 1L;
        Long lokacijaId = 1L;

        DvoranaFormDto dvoranaFormDto = new DvoranaFormDto();
        dvoranaFormDto.setNaziv("Arena2");
        dvoranaFormDto.setLokacijaId(lokacijaId);

        Dvorana existingDvorana = new Dvorana();
        existingDvorana.setDvoranaId(dvoranaId);

        Lokacija lokacija = new Lokacija();
        lokacija.setLokacijaId(lokacijaId);

        when(dvoranaRepository.existsByLokacija_LokacijaIdAndDvoranaIdNot(lokacijaId, dvoranaId)).thenReturn(false);
        when(dvoranaRepository.findById(dvoranaId)).thenReturn(Optional.of(existingDvorana));
        when(lokacijaRepository.findById(lokacijaId)).thenReturn(Optional.of(lokacija));
        when(dvoranaRepository.save(any(Dvorana.class))).thenAnswer(i -> i.getArguments()[0]);

        Dvorana updatedDvorana = dvoranaServiceImpl.update(dvoranaId, dvoranaFormDto);

        Assertions.assertEquals("Arena2", updatedDvorana.getNaziv());
        Assertions.assertEquals(lokacijaId, updatedDvorana.getLokacija().getLokacijaId());
        verify(dvoranaRepository, times(1)).save(any(Dvorana.class));
    }

    @Test
    public void DvoranaService_DeleteDvorana_ThrowsException() {
        Long dvoranaId = 1L;

        doThrow(DataIntegrityViolationException.class).when(dvoranaRepository).deleteById(1L);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> dvoranaServiceImpl.deleteById(dvoranaId)
        );
        verify(dvoranaRepository, never()).delete(any());
    }

    @Test
    public void DvoranaService_SearchDvoraneWhenQueryEmpty_ReturnAllDvorane() {
        dvoranaServiceImpl.search(null);
        dvoranaServiceImpl.search("");

        verify(dvoranaRepository, times(2)).findAll();
        verify(dvoranaRepository, never()).searchLocation(anyString());
    }

}
