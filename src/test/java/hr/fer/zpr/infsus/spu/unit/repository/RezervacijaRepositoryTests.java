package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Kupac;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Rezervacija;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.model.Sjedalo;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.KorisnikRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.repository.RezervacijaRepository;
import hr.fer.zpr.infsus.spu.repository.SektorRepository;
import hr.fer.zpr.infsus.spu.repository.SjedaloRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@DataJpaTest
@ActiveProfiles("test")
public class RezervacijaRepositoryTests {

    @Autowired
    private LokacijaRepository lokacijaRepository;

    @Autowired
    private DvoranaRepository dvoranaRepository;

    @Autowired
    private SektorRepository sektorRepository;

    @Autowired
    private SjedaloRepository sjedaloRepository;

    @Autowired
    private DogadajRepository dogadajRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private RezervacijaRepository rezervacijaRepository;

    private Kupac testKupac;

    private Dogadaj testDogadaj;

    private Sjedalo testSjedalo;

    @BeforeEach
    public void cleanUpRepositoriesBeforeEachTest() {
        rezervacijaRepository.deleteAll();
        dogadajRepository.deleteAll();
        sektorRepository.deleteAll();
        dvoranaRepository.deleteAll();
        lokacijaRepository.deleteAll();
        korisnikRepository.deleteAll();
        testKupac = korisnikRepository.save(EntityFactory.createKupac());
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        Dvorana dvorana = dvoranaRepository.save(EntityFactory.createDvorana("Arena", lokacija));
        testDogadaj = dogadajRepository.save(EntityFactory.createDogadaj("Koncert", dvorana));
        Sektor sektor = sektorRepository.save(EntityFactory.createSektor("Tribina", 100, dvorana));
        testSjedalo = sjedaloRepository.save(EntityFactory.createSjedalo(1, 1, sektor));
    }

    @Test
    public void RezervacijaRepository_SaveAndFindRezervacija_ReturnSavedRezervacija() {
        Rezervacija rezervacija = EntityFactory.createRezervacija(testKupac, testDogadaj, testSjedalo);

        rezervacijaRepository.save(rezervacija);

        Assertions.assertNotNull(rezervacija.getRezervacijaId());
        Assertions.assertTrue(rezervacijaRepository.existsById(rezervacija.getRezervacijaId()));
        Assertions.assertEquals(testKupac.getKorisnikId(), rezervacija.getKupac().getKorisnikId());
    }

    @Test
    public void RezervacijaRepository_FindByDogadajId_ReturnRezervacijaExists() {
        rezervacijaRepository.save(
                EntityFactory.createRezervacija(testKupac, testDogadaj, testSjedalo)
        );

        boolean existsExample = rezervacijaRepository.existsByDogadaj_DogadajId(testDogadaj.getDogadajId());

        boolean notExistsExample = rezervacijaRepository.existsByDogadaj_DogadajId(-1L);

        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void RezervacijaRepository_SaveWithInvalidDatum_ThrowsException() {
        Rezervacija rezervacija = EntityFactory.createRezervacija(testKupac, testDogadaj, testSjedalo);
        rezervacija.setDatumVrijemeIsteka(LocalDateTime.now().minusDays(1));
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> rezervacijaRepository.save(rezervacija)
        );
    }

    @Test
    public void RezervacijaRepository_DeleteExistingRezervacija_ReturnDogadajAndKorisnikAndSjedaloExists() {
        Long rezervacijaId = rezervacijaRepository.save(
                EntityFactory.createRezervacija(testKupac, testDogadaj, testSjedalo)
        ).getRezervacijaId();

        rezervacijaRepository.deleteById(rezervacijaId);

        Assertions.assertFalse(rezervacijaRepository.existsById(rezervacijaId));
        Assertions.assertTrue(korisnikRepository.existsById(testKupac.getKorisnikId()));
        Assertions.assertTrue(dogadajRepository.existsById(testDogadaj.getDogadajId()));
        Assertions.assertTrue(sjedaloRepository.existsById(testSjedalo.getSjedaloId()));
    }

}
