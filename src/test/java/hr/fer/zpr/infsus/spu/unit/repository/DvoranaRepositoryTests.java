package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.repository.SektorRepository;
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
public class DvoranaRepositoryTests {

    @Autowired
    private LokacijaRepository lokacijaRepository;

    @Autowired
    private DvoranaRepository dvoranaRepository;

    @Autowired
    private SektorRepository sektorRepository;

    @BeforeEach
    public void cleanUpRepositoriesBeforeEachTest() {
        dvoranaRepository.deleteAll();
    }

    @Test
    public void DvoranaRepository_ExistsByLokacijaId_ReturnsBoolean() {
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        Dvorana dvorana = EntityFactory.createDvorana("Arena", lokacija);
        dvoranaRepository.save(dvorana);

        boolean existsExample = dvoranaRepository.existsByLokacija_LokacijaId(lokacija.getLokacijaId());
        boolean notExistsExample = dvoranaRepository.existsByLokacija_LokacijaId(-1L);

        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void DvoranaRepository_ExistsByLokacijaIdNotByDvoranaId_ReturnsBoolean() {
        Lokacija lokacija1 = lokacijaRepository.save(EntityFactory.createLokacija());
        Lokacija lokacija2 = lokacijaRepository.save(EntityFactory.createLokacija());
        Dvorana dvorana1 = EntityFactory.createDvorana("Arena", lokacija1);
        dvoranaRepository.save(dvorana1);
        Dvorana dvorana2 = EntityFactory.createDvorana("Hipodrom", lokacija2);
        dvoranaRepository.save(dvorana2);

        boolean existsExample = dvoranaRepository.existsByLokacija_LokacijaIdAndDvoranaIdNot(
                lokacija1.getLokacijaId(), dvorana2.getDvoranaId());
        boolean notExistsExample = dvoranaRepository.existsByLokacija_LokacijaIdAndDvoranaIdNot(
                lokacija1.getLokacijaId(), dvorana1.getDvoranaId());

        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void DvoranaRepository_SearchLocation_ReturnsDvoranaList() {
        Lokacija lokacija1 = EntityFactory.createLokacija();
        Lokacija lokacija2 = EntityFactory.createLokacija();
        lokacija2.setMjesto("Split");
        lokacija2.setPostanskiBroj("21000");
        lokacijaRepository.saveAll(List.of(lokacija1, lokacija2));

        Dvorana dvorana1 = EntityFactory.createDvorana("Koncertna dvorana Vatroslava Lisinskog", lokacija1);
        Dvorana dvorana2 = EntityFactory.createDvorana("Spaladium arena", lokacija2);
        dvoranaRepository.saveAll(List.of(dvorana1, dvorana2));

        Assertions.assertTrue(dvoranaRepository.searchLocation("Osijek").isEmpty());
        Assertions.assertTrue(dvoranaRepository.searchLocation("31000").isEmpty());
        Assertions.assertTrue(dvoranaRepository.searchLocation("zgre").isEmpty());
        Assertions.assertTrue(dvoranaRepository.searchLocation("rijeka").isEmpty());
        Assertions.assertFalse(dvoranaRepository.searchLocation("Spaladium").isEmpty());
        Assertions.assertFalse(dvoranaRepository.searchLocation("zagreb").isEmpty());
        Assertions.assertFalse(dvoranaRepository.searchLocation("21000").isEmpty());
        Assertions.assertFalse(dvoranaRepository.searchLocation("Jarunska").isEmpty());
    }

    @Test
    public void DvoranaRepository_SaveDvoranaWithInvalidNaziv_ThrowsException() {
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        Dvorana dvorana = EntityFactory.createDvorana("Arena", lokacija);
        dvorana.setNaziv("");
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> dvoranaRepository.save(dvorana)
        );
    }

    @Test
    public void DvoranaRepository_SaveDvoranaWithDuplicatedLokacija_ThrowsException() {
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        Dvorana dvorana1 = EntityFactory.createDvorana("Arena", lokacija);
        dvoranaRepository.save(dvorana1);
        Dvorana dvorana2 = EntityFactory.createDvorana("Hipodrom", lokacija);

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> dvoranaRepository.save(dvorana2)
        );
    }

    @Test
    public void DvoranaRepository_SaveAndDeleteCascadingDvoranaSektors_ReturnNothing() {
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());

        Dvorana dvorana = EntityFactory.createDvorana("Arena", lokacija);
        Sektor sektor1 = EntityFactory.createSektor("Tribina A", 100, dvorana);
        Sektor sektor2 = EntityFactory.createSektor("Tribina B", 150, dvorana);
        dvorana.setSektori(List.of(sektor1, sektor2));

        dvoranaRepository.save(dvorana);

        Assertions.assertEquals(2, dvorana.getSektori().size());
        Assertions.assertEquals(2, sektorRepository.count());

        dvoranaRepository.deleteById(dvorana.getDvoranaId());

        Assertions.assertEquals(0, dvoranaRepository.count());
        Assertions.assertEquals(0, sektorRepository.count());
    }

}
