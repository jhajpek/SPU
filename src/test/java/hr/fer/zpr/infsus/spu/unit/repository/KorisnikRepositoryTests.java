package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Korisnik;
import hr.fer.zpr.infsus.spu.repository.KorisnikRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class KorisnikRepositoryTests {

    @Autowired
    private KorisnikRepository korisnikRepository;
    
    @BeforeEach
    public void cleanUpRepositoryBeforeEachTest() {
        korisnikRepository.deleteAll();
    }

    @Test
    public void KorisnikRepository_SaveAndFindKorisnik_ReturnSavedKorisnik() {
        Korisnik korisnik = EntityFactory.createKorisnik();

        Korisnik savedKorisnik = korisnikRepository.save(korisnik);
        Assertions.assertNotNull(savedKorisnik);

        boolean isSavedKorisnikInKorisnikRepository = korisnikRepository.existsById(savedKorisnik.getKorisnikId());
        Assertions.assertTrue(isSavedKorisnikInKorisnikRepository);
    }

    @Test
    public void KorisnikRepository_SaveAndFindAllKorisnici_ReturnKorisnikList() {
        Korisnik korisnik1 = EntityFactory.createKorisnik();
        Korisnik korisnik2 = EntityFactory.createKorisnik();
        korisnik2.setEmail("marko.mamic2@gmail.com");
        korisnikRepository.saveAll(List.of(korisnik1, korisnik2));

        long numberOfKorisnika = korisnikRepository.count();
        Assertions.assertEquals(2, numberOfKorisnika);
    }

    @Test
    public void KorisnikRepository_FindKorisnikByEmail_ReturnKorisnikExists() {
        Korisnik korisnik = EntityFactory.createKorisnik();
        korisnikRepository.save(korisnik);

        boolean email1Exists = korisnikRepository.existsByEmail("marko.mamic@gmail.com");
        boolean email2Exists = korisnikRepository.existsByEmail("matko.mamic@gmail.com");

        Assertions.assertTrue(email1Exists);
        Assertions.assertFalse(email2Exists);
    }

    @Test
    public void KorisnikRepository_SaveKorisnikWithInvalidIme_ThrowsException() {
        Korisnik korisnik = EntityFactory.createKorisnik();
        korisnik.setIme("");
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> korisnikRepository.save(korisnik)
        );
    }

    @Test
    public void KorisnikRepository_SaveDuplicatedKorisnik_ThrowsException() {
        Korisnik korisnik1 = EntityFactory.createKorisnik();
        korisnikRepository.save(korisnik1);

        Korisnik korisnik2 = EntityFactory.createKorisnik();

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> korisnikRepository.save(korisnik2)
        );
    }

    @Test
    public void KorisnikRepository_DeleteExistingKorisnik_ReturnKorisnikNotExists() {
        Korisnik korisnik = EntityFactory.createKorisnik();

        korisnikRepository.save(korisnik);
        Long korisnikId = korisnik.getKorisnikId();

        korisnikRepository.deleteById(korisnikId);

        boolean isSavedKorisnikInKorisnikRepository = korisnikRepository.existsById(korisnikId);
        Assertions.assertFalse(isSavedKorisnikInKorisnikRepository);
    }

}
