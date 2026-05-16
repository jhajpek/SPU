package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Kupac;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.model.Sjedalo;
import hr.fer.zpr.infsus.spu.model.Ulaznica;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.KorisnikRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.repository.SektorRepository;
import hr.fer.zpr.infsus.spu.repository.SjedaloRepository;
import hr.fer.zpr.infsus.spu.repository.UlaznicaRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class UlaznicaRepositoryTests {

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
    private UlaznicaRepository ulaznicaRepository;

    private Kupac testKupac;

    private Dogadaj testDogadaj;

    private Sektor testSektor;

    private Sjedalo testSjedalo1;

    private Sjedalo testSjedalo2;

    @BeforeEach
    public void cleanUpRepositoriesBeforeEachTest() {
        ulaznicaRepository.deleteAll();
        dogadajRepository.deleteAll();
        sektorRepository.deleteAll();
        dvoranaRepository.deleteAll();
        lokacijaRepository.deleteAll();
        korisnikRepository.deleteAll();
        testKupac = korisnikRepository.save(EntityFactory.createKupac());
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        Dvorana dvorana = dvoranaRepository.save(EntityFactory.createDvorana("Arena", lokacija));
        testDogadaj = dogadajRepository.save(EntityFactory.createDogadaj("Koncert", dvorana));
        testSektor= sektorRepository.save(EntityFactory.createSektor("Tribina", 100, dvorana));
        testSjedalo1 = sjedaloRepository.save(EntityFactory.createSjedalo(1, 1, testSektor));
        testSjedalo2 = sjedaloRepository.save(EntityFactory.createSjedalo(1, 2, testSektor));
    }

    @Test
    public void UlaznicaRepository_CountUlazniceByDogadajAndSektor_ReturnUlazniceCount() {
        ulaznicaRepository.save(EntityFactory.createUlaznica(testKupac, testDogadaj, testSjedalo1));
        ulaznicaRepository.save(EntityFactory.createUlaznica(testKupac, testDogadaj, testSjedalo2));

        long numberOfUlaznice = ulaznicaRepository.countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(
                testDogadaj.getDogadajId(), testSektor.getSektorId()
        );

        Assertions.assertEquals(2, numberOfUlaznice);
    }

    @Test
    public void UlaznicaRepository_FindByDogadajId_ReturnUlaznicaExists() {
        ulaznicaRepository.save(EntityFactory.createUlaznica(testKupac, testDogadaj, testSjedalo1));

        boolean existsExample = ulaznicaRepository.existsByDogadaj_DogadajId(testDogadaj.getDogadajId());
        boolean notExistsExample = ulaznicaRepository.existsByDogadaj_DogadajId(-1L);

        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void UlaznicaRepository_FindUlaznicaBySektorId_ReturnUlaznicaExists() {
        ulaznicaRepository.save(EntityFactory.createUlaznica(testKupac, testDogadaj, testSjedalo1));

        Assertions.assertTrue(ulaznicaRepository.existsBySjedalo_Sektor_SektorId(testSektor.getSektorId()));
    }

    @Test
    public void UlaznicaRepository_SaveUlaznicaWithDuplicatedQR_ThrowsException() {
        Ulaznica ulaznica1 = ulaznicaRepository.save(EntityFactory.createUlaznica(testKupac, testDogadaj, testSjedalo1));
        Ulaznica ulaznica2 = EntityFactory.createUlaznica(testKupac, testDogadaj, testSjedalo2);
        ulaznica2.setQrKod(ulaznica1.getQrKod());

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> ulaznicaRepository.save(ulaznica2)
        );
    }

    @Test
    public void UlaznicaRepository_SaveUlaznica_CreationTimestampActivates() {
        Ulaznica ulaznica = ulaznicaRepository.save(EntityFactory.createUlaznica(testKupac, testDogadaj, testSjedalo1));

        Assertions.assertNotNull(ulaznica.getDatumVrijemeKupnje());
    }

}
