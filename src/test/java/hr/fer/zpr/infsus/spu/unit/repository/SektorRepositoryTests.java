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
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class SektorRepositoryTests {

    @Autowired
    private LokacijaRepository lokacijaRepository;

    @Autowired
    private DvoranaRepository dvoranaRepository;

    @Autowired
    private SektorRepository sektorRepository;

    private Dvorana testDvorana;

    @BeforeEach
    public void cleanUpRepositoriesBeforeEachTest() {
        dvoranaRepository.deleteAll();
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        testDvorana = dvoranaRepository.save(EntityFactory.createDvorana("Arena", lokacija));
    }

    @Test
    public void SektorRepository_FindAllSektoriByNaziv_ReturnSortedSektorList() {
        sektorRepository.save(EntityFactory.createSektor("Istok 1", 300, testDvorana));
        sektorRepository.save(EntityFactory.createSektor("Istok 2", 200, testDvorana));
        sektorRepository.save(EntityFactory.createSektor("Jug", 150, testDvorana));

        List<Sektor> sortedSektors = sektorRepository.findAllByNazivContainsIgnoreCaseOrderByDvorana_Naziv("istok");

        Assertions.assertEquals(2, sortedSektors.size());
        Assertions.assertTrue(sortedSektors.stream().allMatch(s -> s.getNaziv().toLowerCase().contains("istok")));
        if (sortedSektors.size() == 2) {
            String first = sortedSektors.get(0).getDvorana().getNaziv();
            String second = sortedSektors.get(1).getDvorana().getNaziv();
            Assertions.assertTrue(first.compareTo(second) <= 0);
        }
    }

    @Test
    public void SektorRepository_FindAllSektoriByDvoranaId_ReturnSortedSektorList() {
        sektorRepository.save(EntityFactory.createSektor("Zapad", 300, testDvorana));
        sektorRepository.save(EntityFactory.createSektor("Istok", 300, testDvorana));

        List<Sektor> sortedSektors = sektorRepository.findAllByDvorana_DvoranaIdOrderByNaziv(testDvorana.getDvoranaId());

        Assertions.assertEquals(2, sortedSektors.size());
        Assertions.assertEquals("Istok", sortedSektors.get(0).getNaziv());
        Assertions.assertEquals("Zapad", sortedSektors.get(1).getNaziv());
    }

    @Test
    public void SektorRepository_FindAllSektoriByNazivAndDvoranaId_ReturnSortedSektorList() {
        sektorRepository.save(EntityFactory.createSektor("Istok 1", 300, testDvorana));
        sektorRepository.save(EntityFactory.createSektor("Istok 2", 200, testDvorana));
        sektorRepository.save(EntityFactory.createSektor("Jug", 150, testDvorana));

        List<Sektor> sortedSektors = sektorRepository.findAllByNazivContainsIgnoreCaseAndDvorana_DvoranaIdOrderByNaziv(
                "Isto", testDvorana.getDvoranaId()
        );

        Assertions.assertEquals(2, sortedSektors.size());
        Assertions.assertTrue(sortedSektors.stream().allMatch(s -> s.getNaziv().toLowerCase().contains("isto")));
        Assertions.assertEquals("Istok 1", sortedSektors.get(0).getNaziv());
        Assertions.assertEquals("Istok 2", sortedSektors.get(1).getNaziv());
    }

    @Test
    public void SektorRepository_FindSektorByNazivAndDvoranaId_ReturnSektorExists() {
        sektorRepository.save(EntityFactory.createSektor("VIP", 50, testDvorana));

        boolean existsExample = sektorRepository.existsByNazivIgnoreCaseAndDvorana_DvoranaId("vip", testDvorana.getDvoranaId());
        boolean notExistsExample = sektorRepository.existsByNazivIgnoreCaseAndDvorana_DvoranaId("VIP", -1L);

        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void SektorRepository_FindSektorByNazivAndDvoranaIdAndSektorIdNot_ReturnSektorExists() {
        Sektor sektor1 = sektorRepository.save(EntityFactory.createSektor("VIP", 50, testDvorana));
        Sektor sektor2 = sektorRepository.save(EntityFactory.createSektor("Jug", 100, testDvorana));

        boolean existsExample = sektorRepository.existsByNazivIgnoreCaseAndDvorana_DvoranaIdAndSektorIdNot(
                "VIP", testDvorana.getDvoranaId(), sektor2.getSektorId()
        );

        boolean notExistsExample = sektorRepository.existsByNazivIgnoreCaseAndDvorana_DvoranaIdAndSektorIdNot(
                "VIP", testDvorana.getDvoranaId(), sektor1.getSektorId()
        );

        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void SektorRepository_SaveSektorWithInvalidNaziv_ThrowsException() {
        Sektor sektor = EntityFactory.createSektor("", 0, testDvorana);
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> sektorRepository.save(sektor)
        );
    }

    @Test
    public void SektorRepository_SaveSektorWithInvalidKapacitet_ThrowsException() {
        Sektor sektor = EntityFactory.createSektor("VIP", 0, testDvorana);
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> sektorRepository.save(sektor)
        );
    }

}
