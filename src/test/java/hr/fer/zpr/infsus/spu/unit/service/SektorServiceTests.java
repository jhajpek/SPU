package hr.fer.zpr.infsus.spu.unit.service;

import hr.fer.zpr.infsus.spu.dto.SektorFormDto;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.repository.CjenikRepository;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.SektorRepository;
import hr.fer.zpr.infsus.spu.repository.UlaznicaRepository;
import hr.fer.zpr.infsus.spu.service.impl.SektorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

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
public class SektorServiceTests {

    @Mock
    private DvoranaRepository dvoranaRepository;

    @Mock
    private SektorRepository sektorRepository;

    @Mock
    private CjenikRepository cjenikRepository;

    @Mock
    private UlaznicaRepository ulaznicaRepository;

    @InjectMocks
    private SektorServiceImpl sektorServiceImpl;

    @Test
    public void SektorService_SaveSektor_ReturnSavedSektor() {
        SektorFormDto sektorFormDto = new SektorFormDto();
        sektorFormDto.setNaziv("Tribina A");
        sektorFormDto.setDvoranaId(1L);
        sektorFormDto.setKapacitet(500);

        Dvorana dvorana = new Dvorana();
        dvorana.setDvoranaId(1L);

        when(sektorRepository.existsByNazivIgnoreCaseAndDvorana_DvoranaId(anyString(), anyLong())).thenReturn(false);
        when(dvoranaRepository.findById(1L)).thenReturn(Optional.of(dvorana));
        when(sektorRepository.save(any(Sektor.class))).thenAnswer(i -> i.getArguments()[0]);

        Sektor savedSektor = sektorServiceImpl.save(sektorFormDto);

        Assertions.assertNotNull(savedSektor);
        Assertions.assertEquals("Tribina A", savedSektor.getNaziv());
        verify(sektorRepository, times(1)).save(any(Sektor.class));
    }

    @Test
    public void SektorService_SaveSektor_ThrowsException() {
        SektorFormDto sektorFormDto = new SektorFormDto();
        sektorFormDto.setNaziv("Tribina A");
        sektorFormDto.setDvoranaId(1L);

        when(sektorRepository.existsByNazivIgnoreCaseAndDvorana_DvoranaId(anyString(), anyLong())).thenReturn(true);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> sektorServiceImpl.save(sektorFormDto)
        );
        verify(sektorRepository, never()).save(any(Sektor.class));
    }

    @Test
    public void SektorService_UpdateSektor_ThrowsException() {
        Long sektorId = 1L;
        Long dvoranaId = 1L;
        Long newDvoranaId = 2L;

        SektorFormDto sektorFormDto = new SektorFormDto();
        sektorFormDto.setNaziv("Tribina A");
        sektorFormDto.setDvoranaId(newDvoranaId);

        Dvorana dvorana = new Dvorana();
        dvorana.setDvoranaId(dvoranaId);

        Sektor sektor = new Sektor();
        sektor.setSektorId(sektorId);
        sektor.setDvorana(dvorana);

        when(sektorRepository.findById(sektorId)).thenReturn(Optional.of(sektor));
        when(dvoranaRepository.findById(newDvoranaId)).thenReturn(Optional.of(new Dvorana()));
        when(ulaznicaRepository.existsBySjedalo_Sektor_SektorId(sektorId)).thenReturn(true);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> sektorServiceImpl.update(sektorId, sektorFormDto)
        );
        verify(sektorRepository, never()).save(any(Sektor.class));
    }

    @Test
    public void SektorService_SearchSektori_ReturnAllSektori() {
        sektorServiceImpl.search(null, null);

        verify(sektorRepository, times(1)).findAll();
    }

    @Test
    public void SektorService_SearchSektoriByNazivAndDvorana_ReturnFilteredSektorList() {
        sektorServiceImpl.search("Tribina A", 1L);

        verify(sektorRepository, times(1)).findAllByNazivContainsIgnoreCaseAndDvorana_DvoranaIdOrderByNaziv("Tribina A", 1L);
    }

    @Test
    public void SektorService_DeleteSektor_ThrowsException() {
        Long sektorId = 1L;

        doThrow(DataIntegrityViolationException.class).when(sektorRepository).deleteById(sektorId);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> sektorServiceImpl.deleteById(sektorId)
        );
        verify(sektorRepository, never()).delete(any());
    }

}
