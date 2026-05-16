package hr.fer.zpr.infsus.spu.unit.service;

import hr.fer.zpr.infsus.spu.dto.CjenikFormDto;
import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.repository.CjenikRepository;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.SektorRepository;
import hr.fer.zpr.infsus.spu.repository.UlaznicaRepository;
import hr.fer.zpr.infsus.spu.service.impl.CjenikServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CjenikServiceTests {

    @Mock
    private SektorRepository sektorRepository;

    @Mock
    private UlaznicaRepository ulaznicaRepository;

    @Mock
    private CjenikRepository cjenikRepository;

    @Mock
    private DogadajRepository dogadajRepository;

    @InjectMocks
    private CjenikServiceImpl cjenikService;

    @Test
    public void CjenikService_FindAvailableSektori_ReturnSektorList() {
        Long dogadajId = 1L;
        Long dvoranaId = 1L;

        Dvorana dvorana = new Dvorana();
        dvorana.setDvoranaId(dvoranaId);

        Dogadaj dogadaj = new Dogadaj();
        dogadaj.setDvorana(dvorana);
        dogadaj.setCjenici(new ArrayList<>());

        Sektor sektor1 = new Sektor();
        sektor1.setSektorId(1L);

        Sektor sektor2 = new Sektor();
        sektor2.setSektorId(2L);

        Cjenik cjenik = new Cjenik();
        cjenik.setSektor(sektor1);

        dogadaj.getCjenici().add(cjenik);

        when(dogadajRepository.findById(dogadajId)).thenReturn(Optional.of(dogadaj));
        when(sektorRepository.findAllByDvorana_DvoranaIdOrderByNaziv(dvoranaId)).thenReturn(List.of(sektor1, sektor2));

        List<Sektor> sektori = cjenikService.findAvailableSectors(dogadajId);

        Assertions.assertEquals(1, sektori.size());
        Assertions.assertEquals(2L, sektori.get(0).getSektorId());
        verify(dogadajRepository, times(1)).findById(dogadajId);
        verify(sektorRepository, times(1)).findAllByDvorana_DvoranaIdOrderByNaziv(dvoranaId);
    }

    @Test
    public void CjenikService_SaveCjenik_ReturnSavedCjenik() {
        Dvorana dvorana = new Dvorana();
        dvorana.setDvoranaId(1L);

        Dogadaj dogadaj = new Dogadaj();
        dogadaj.setDogadajId(1L);
        dogadaj.setDvorana(dvorana);

        Sektor sektor = new Sektor();
        sektor.setSektorId(1L);
        sektor.setDvorana(dvorana);

        CjenikFormDto cjenikFormDto = new CjenikFormDto();
        cjenikFormDto.setDogadajId(1L);
        cjenikFormDto.setSektorId(1L);
        cjenikFormDto.setCijena(new BigDecimal("30.00"));

        when(cjenikRepository.existsByDogadaj_DogadajIdAndSektor_SektorId(1L, 1L)).thenReturn(false);
        when(dogadajRepository.findById(1L)).thenReturn(Optional.of(dogadaj));
        when(sektorRepository.findById(1L)).thenReturn(Optional.of(sektor));
        when(cjenikRepository.save(any(Cjenik.class))).thenAnswer(i -> i.getArguments()[0]);

        Cjenik savedCjenik = cjenikService.save(cjenikFormDto);

        Assertions.assertNotNull(savedCjenik);
        Assertions.assertEquals(new BigDecimal("30.00"), savedCjenik.getCijena());
        verify(cjenikRepository, times(1)).save(any(Cjenik.class));
    }

    @Test
    public void CjenikService_SaveCjenik_ThrowsException() {
        Dvorana dvorana1 = new Dvorana();
        dvorana1.setDvoranaId(1L);

        Dvorana dvorana2 = new Dvorana();
        dvorana2.setDvoranaId(2L);

        Dogadaj dogadaj = new Dogadaj();
        dogadaj.setDogadajId(1L);
        dogadaj.setDvorana(dvorana1);

        Sektor sektor = new Sektor();
        sektor.setSektorId(1L);
        sektor.setDvorana(dvorana2);

        CjenikFormDto cjenikFormDto = new CjenikFormDto();
        cjenikFormDto.setDogadajId(1L);
        cjenikFormDto.setSektorId(1L);

        when(cjenikRepository.existsByDogadaj_DogadajIdAndSektor_SektorId(anyLong(), anyLong())).thenReturn(false);
        when(dogadajRepository.findById(1L)).thenReturn(Optional.of(dogadaj));
        when(sektorRepository.findById(1L)).thenReturn(Optional.of(sektor));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> cjenikService.save(cjenikFormDto)
        );
        verify(cjenikRepository, never()).save(any(Cjenik.class));
    }

    @Test
    public void CjenikService_DeleteCjenik_ThrowsException() {
        Long dogadajId = 1L;
        Dogadaj dogadaj = new Dogadaj();
        dogadaj.setDogadajId(dogadajId);

        Long sektorId = 2L;
        Sektor sektor = new Sektor();
        sektor.setSektorId(sektorId);

        Long cjenikId = 1L;
        Cjenik cjenik = new Cjenik();
        cjenik.setCjenikId(cjenikId);
        cjenik.setDogadaj(dogadaj);
        cjenik.setSektor(sektor);

        when(cjenikRepository.findById(cjenikId)).thenReturn(Optional.of(cjenik));
        when(ulaznicaRepository.existsByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(dogadajId, sektorId)).thenReturn(true);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> cjenikService.deleteById(cjenikId)
        );
        verify(cjenikRepository, never()).delete(any());
    }

}
