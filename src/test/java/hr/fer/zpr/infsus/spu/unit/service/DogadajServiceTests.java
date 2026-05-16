package hr.fer.zpr.infsus.spu.unit.service;

import hr.fer.zpr.infsus.spu.dto.DogadajFormDto;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.RezervacijaRepository;
import hr.fer.zpr.infsus.spu.repository.UlaznicaRepository;
import hr.fer.zpr.infsus.spu.service.impl.DogadajServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DogadajServiceTests {

    @Mock
    private DvoranaRepository dvoranaRepository;

    @Mock
    private RezervacijaRepository rezervacijaRepository;

    @Mock
    private UlaznicaRepository ulaznicaRepository;

    @Mock
    private DogadajRepository dogadajRepository;

    @InjectMocks
    private DogadajServiceImpl dogadajServiceImpl;

    @Test
    public void DogadajService_SaveDogadaj_ReturnSavedDogadaj() {
        Dvorana dvorana = new Dvorana();
        dvorana.setDvoranaId(1L);

        DogadajFormDto dogadajFormDto = new DogadajFormDto();
        dogadajFormDto.setDvoranaId(1L);
        dogadajFormDto.setDatumVrijemeOdrzavanja(LocalDateTime.now().plusDays(5));

        when(dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(anyLong(), any())).thenReturn(false);
        when(dvoranaRepository.findById(1L)).thenReturn(Optional.of(dvorana));
        when(dogadajRepository.save(any(Dogadaj.class))).thenAnswer(i -> i.getArguments()[0]);

        Dogadaj dogadaj = dogadajServiceImpl.save(dogadajFormDto);

        Assertions.assertNotNull(dogadaj);
        Assertions.assertEquals(1L, dogadaj.getDvorana().getDvoranaId());
        verify(dogadajRepository, times(1)).save(any(Dogadaj.class));
    }

    @Test
    public void DogadajService_SaveDogadajWithExistingDatumForDvorana_ThrowsException() {
        DogadajFormDto dogadajFormDto = new DogadajFormDto();
        dogadajFormDto.setDvoranaId(1L);
        dogadajFormDto.setDatumVrijemeOdrzavanja(LocalDateTime.now().plusDays(5));

        when(dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(anyLong(), any())).thenReturn(true);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> dogadajServiceImpl.save(dogadajFormDto)
        );
        verify(dogadajRepository, never()).save(any(Dogadaj.class));
    }

    @Test
    public void DogadajService_SaveDogadajWithInvalidDatum_ThrowsException() {
        DogadajFormDto dogadajFormDto = new DogadajFormDto();
        dogadajFormDto.setDatumVrijemeOdrzavanja(LocalDateTime.now().minusDays(1));

        when(dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(any(), any())).thenReturn(false);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> dogadajServiceImpl.save(dogadajFormDto)
        );
        verify(dogadajRepository, never()).save(any(Dogadaj.class));
    }

    @Test
    public void DogadajService_UpdateDogadaj_ThrowsException() {
        Long dogadajId = 1L;

        Long dvoranaId = 1L;
        Long newDvoranaId = 2L;

        DogadajFormDto dogadajFormDto = new DogadajFormDto();
        dogadajFormDto.setDvoranaId(newDvoranaId);
        dogadajFormDto.setDatumVrijemeOdrzavanja(LocalDateTime.now().plusDays(10));

        Dogadaj existingDogadaj = new Dogadaj();
        existingDogadaj.setDogadajId(dogadajId);

        Dvorana dvorana = new Dvorana();
        dvorana.setDvoranaId(dvoranaId);
        existingDogadaj.setDvorana(dvorana);

        when(dogadajRepository.findById(dogadajId)).thenReturn(Optional.of(existingDogadaj));
        when(dvoranaRepository.findById(newDvoranaId)).thenReturn(Optional.of(new Dvorana()));
        when(ulaznicaRepository.existsByDogadaj_DogadajId(dogadajId)).thenReturn(true);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> dogadajServiceImpl.update(dogadajId, dogadajFormDto)
        );
        verify(dogadajRepository, never()).save(any(Dogadaj.class));
    }

    @Test
    public void DogadajService_SearchDogadajiByNazivAndKategorija_ReturnFilteredDogadajList() {
        String naziv = "Dogadaj 1";
        String kategorija = "Sport";

        dogadajServiceImpl.search(naziv, kategorija);

        verify(dogadajRepository, times(1)).findAllByNazivContainsIgnoreCaseAndKategorijaContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(eq(naziv), eq(kategorija), any());
    }

    @Test
    public void DogadajService_SearchDogadajiByNaziv_ReturnFilteredDogadajList() {
        String naziv = "Dogadaj 1";

        dogadajServiceImpl.search(naziv, "");

        verify(dogadajRepository, times(1)).findAllByNazivContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(eq(naziv), any());
    }

    @Test
    public void DogadajService_SearchDogadajiByKategorija_ReturnFilteredDogadajList() {
        String kategorija = "glazba";

        dogadajServiceImpl.search("", kategorija);

        verify(dogadajRepository, times(1)).findAllByKategorijaContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(eq(kategorija), any());
    }

    @Test
    public void DogadajService_SearchDogadaji_ReturnDogadajList() {
        dogadajServiceImpl.search("  ", null);

        verify(dogadajRepository, times(1)).findAllByDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(any());
    }

    @Test
    public void DogadajService_DeleteDogadaj_ReturnDogadajNotExists() {
        Long dogadajId = 1L;

        doNothing().when(dogadajRepository).deleteById(dogadajId);

        dogadajServiceImpl.deleteById(dogadajId);

        verify(dogadajRepository, times(1)).deleteById(dogadajId);
    }

}
