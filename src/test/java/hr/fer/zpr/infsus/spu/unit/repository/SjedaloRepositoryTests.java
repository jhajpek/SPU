package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.model.Sjedalo;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
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

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class SjedaloRepositoryTests {

    @Autowired
    private LokacijaRepository lokacijaRepository;

    @Autowired
    private DvoranaRepository dvoranaRepository;

    @Autowired
    private SektorRepository sektorRepository;

    @Autowired
    private SjedaloRepository sjedaloRepository;

    private Sektor testSektor;

    @BeforeEach
    public void setUpTestEntities() {
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        Dvorana dvorana = dvoranaRepository.save(EntityFactory.createDvorana("Arena", lokacija));
        testSektor = sektorRepository.save(EntityFactory.createSektor("Tribina", 200, dvorana));
    }

    @Test
    public void SjedaloRepository_SaveAndFindSjedalo_ReturnSavedSjedalo() {
        Sjedalo sjedalo = EntityFactory.createSjedalo(1, 10, testSektor);

        sjedaloRepository.save(sjedalo);
        Assertions.assertNotNull(sjedalo);

        boolean isSavedSjedaloInSjedaloRepository = sjedaloRepository.existsById(sjedalo.getSjedaloId());
        Assertions.assertTrue(isSavedSjedaloInSjedaloRepository);
    }

    @Test
    public void SjedaloRepository_SaveAndFindAllSjedala_ReturnSjedaloList() {
        Sjedalo s1 = EntityFactory.createSjedalo(1, 1, testSektor);
        Sjedalo s2 = EntityFactory.createSjedalo(1, 2, testSektor);
        sjedaloRepository.saveAll(List.of(s1, s2));
        Assertions.assertEquals(2, sjedaloRepository.count());
    }

    @Test
    public void SjedaloRepository_SaveSjedaloWithInvalidRed_ThrowsException() {
        Sjedalo sjedalo = EntityFactory.createSjedalo(-1, 10, testSektor);
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> sjedaloRepository.save(sjedalo)
        );
    }

    @Test
    public void SjedaloRepository_SaveSjedaloWithInvalidBroj_ThrowsException() {
        Sjedalo sjedalo = EntityFactory.createSjedalo(1, 0, testSektor);
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> sjedaloRepository.save(sjedalo)
        );
    }

    @Test
    public void SjedaloRepository_DeleteExistingSjedalo_ReturnSjedaloNotExists() {
        Sjedalo sjedalo = EntityFactory.createSjedalo(1, 1, testSektor);
        sjedaloRepository.save(sjedalo);

        Long sjedaloId = sjedalo.getSjedaloId();
        sjedaloRepository.deleteById(sjedaloId);

        boolean isSavedSjedaloInSjedaloRepository = sjedaloRepository.existsById(sjedaloId);
        Assertions.assertFalse(isSavedSjedaloInSjedaloRepository);
    }

}
