package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class DogadajRepositoryTests {

    @Autowired
    private LokacijaRepository lokacijaRepository;

    @Autowired
    private DvoranaRepository dvoranaRepository;

    @Autowired
    private DogadajRepository dogadajRepository;

    private Dvorana testDvorana;

    @BeforeEach
    public void setUpTestEntities() {
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        testDvorana = dvoranaRepository.save(EntityFactory.createDvorana("Arena", lokacija));
    }

    @Test
    public void DogadajRepository_FindAllUpcomingDogadaji_ReturnDogadajList() {
        Dogadaj dogadaj1 = EntityFactory.createDogadaj("Dogadaj1", testDvorana);
        dogadaj1.setDatumVrijemeOdrzavanja(LocalDateTime.now().plusDays(1));

        Dogadaj dogadaj2 = EntityFactory.createDogadaj("Dogadaj2", testDvorana);
        dogadaj2.setDatumVrijemeOdrzavanja(LocalDateTime.now().plusDays(3));

        dogadajRepository.saveAll(List.of(dogadaj1, dogadaj2));

        List<Dogadaj> dogadaji = dogadajRepository.findAllByDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
                LocalDateTime.now().plusDays(2)
        );

        Assertions.assertEquals(1, dogadaji.size());
        Assertions.assertEquals("Dogadaj2", dogadaji.get(0).getNaziv());
    }

    @Test
    public void DogadajRepository_FindAllDogadajiByNazivAndDatum_ReturnSortedDogadajList() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime dayAfter = LocalDateTime.now().plusDays(2);

        Dogadaj dogadaj1 = EntityFactory.createDogadaj("Dogadaj1", testDvorana);
        dogadaj1.setDatumVrijemeOdrzavanja(dayAfter);

        Dogadaj dogadaj2 = EntityFactory.createDogadaj("Dogadaj2", testDvorana);
        dogadaj2.setDatumVrijemeOdrzavanja(tomorrow);

        dogadajRepository.saveAll(List.of(dogadaj1, dogadaj2));

        List<Dogadaj> dogadaji1 = dogadajRepository.findAllByNazivContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
                "1", LocalDateTime.now()
        );

        Assertions.assertEquals(1, dogadaji1.size());
        Assertions.assertEquals("Dogadaj1", dogadaji1.get(0).getNaziv());

        List<Dogadaj> dogadaji2 = dogadajRepository.findAllByNazivContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
                "dog", LocalDateTime.now()
        );

        Assertions.assertEquals(2, dogadaji2.size());
        Assertions.assertEquals("Dogadaj2", dogadaji2.get(0).getNaziv());
    }

    @Test
    public void DogadajRepository_FindAllDogadajiByKategorijaAndDatum_ReturnSortedDogadajList() {
        Dogadaj dogadaj1 = EntityFactory.createDogadaj("Dogadaj1", testDvorana);
        dogadaj1.setKategorija("Glazba");

        Dogadaj dogadaj2 = EntityFactory.createDogadaj("Dogadaj2", testDvorana);
        dogadaj2.setKategorija("Sport");

        dogadajRepository.saveAll(List.of(dogadaj1, dogadaj2));

        List<Dogadaj> glazbeniDogadaji = dogadajRepository.findAllByKategorijaContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
                "glazba", LocalDateTime.now()
        );

        Assertions.assertEquals(1, glazbeniDogadaji.size());
        Assertions.assertEquals("Glazba", glazbeniDogadaji.get(0).getKategorija());
        Assertions.assertEquals("Dogadaj1", glazbeniDogadaji.get(0).getNaziv());
    }

    @Test
    public void DogadajRepository_FindByDvoranaIdAndDatum_ReturnsDogadajExists() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(7).withNano(0);
        Dogadaj dogadaj = EntityFactory.createDogadaj("Koncert", testDvorana);
        dogadaj.setDatumVrijemeOdrzavanja(dateTime);
        dogadajRepository.save(dogadaj);

        boolean existsExample = dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(
                testDvorana.getDvoranaId(), dateTime
        );

        boolean notExistsExample = dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(
                testDvorana.getDvoranaId(), dateTime.plusHours(1)
        );

        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void DogadajRepository_FindByDvoranaIdAndDatumAndNotDogadajId_ReturnsDogadajExists() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(15).withNano(0);
        Dogadaj dogadaj1 = EntityFactory.createDogadaj("Koncert", testDvorana);
        dogadajRepository.save(dogadaj1);

        Dogadaj dogadaj2 = EntityFactory.createDogadaj("Koncert", testDvorana);
        dogadaj2.setDatumVrijemeOdrzavanja(dateTime);
        dogadajRepository.save(dogadaj2);

        boolean existsExample = dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanjaAndDogadajIdNot(
                testDvorana.getDvoranaId(), dateTime, dogadaj1.getDogadajId()
        );

        boolean notExistsExample = dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanjaAndDogadajIdNot(
                testDvorana.getDvoranaId(), dogadaj1.getDatumVrijemeOdrzavanja(), dogadaj1.getDogadajId()
        );

        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void DogadajRepository_SaveDogadajWithInvalidNaziv_ThrowsException() {
        Dogadaj dogadaj = EntityFactory.createDogadaj("Dogadaj", testDvorana);
        dogadaj.setNaziv("");
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> dogadajRepository.save(dogadaj)
        );
    }

    @Test
    public void DogadajRepository_SaveDogadajWithInvalidDatum_ThrowsException() {
        Dogadaj dogadaj = EntityFactory.createDogadaj("Dogadaj", testDvorana);
        dogadaj.setDatumVrijemeOdrzavanja(LocalDateTime.now().minusDays(1));
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> dogadajRepository.save(dogadaj)
        );
    }

}
