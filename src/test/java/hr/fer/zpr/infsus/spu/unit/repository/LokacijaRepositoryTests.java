package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
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
public class LokacijaRepositoryTests {

    @Autowired
    private LokacijaRepository lokacijaRepository;

    @BeforeEach
    public void cleanUpRepositoryBeforeEachTest() {
        lokacijaRepository.deleteAll();
    }

    @Test
    public void LokacijaRepository_SaveAndFindAllLokacije_ReturnLokacijaList() {
        Lokacija lokacija1 = EntityFactory.createLokacija();
        Lokacija lokacija2 = EntityFactory.createLokacija();
        lokacija2.setKucniBroj(3);
        lokacijaRepository.saveAll(List.of(lokacija1, lokacija2));

        long numberOfLokacija = lokacijaRepository.count();
        Assertions.assertEquals(2, numberOfLokacija);
    }

    @Test
    public void LokacijaRepository_SaveLokacijaWithInvalidUlica_ThrowsException() {
        Lokacija lokacija = EntityFactory.createLokacija();
        lokacija.setUlica("");
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> lokacijaRepository.save(lokacija)
        );
    }

    @Test
    public void LokacijaRepository_SaveLokacijaWithInvalidKucniBroj_ThrowsException() {
        Lokacija lokacija = EntityFactory.createLokacija();
        lokacija.setKucniBroj(-1);
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> lokacijaRepository.save(lokacija)
        );
    }

    @Test
    public void LokacijaRepository_SaveLokacijaWithInvalidPostanskiBroj_ThrowsException() {
        Lokacija lokacija = EntityFactory.createLokacija();
        lokacija.setPostanskiBroj("A10000");
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> lokacijaRepository.save(lokacija)
        );
    }

    @Test
    public void LokacijaRepository_FindDistinctMjesta_ReturnMjestoList() {
        Lokacija lokacija1 = EntityFactory.createLokacija();
        Lokacija lokacija2 = EntityFactory.createLokacija();
        lokacija2.setMjesto("Rijeka");
        Lokacija lokacija3 = EntityFactory.createLokacija();
        lokacija3.setMjesto("Split");
        Lokacija lokacija4 = EntityFactory.createLokacija();

        lokacijaRepository.saveAll(List.of(lokacija1, lokacija2, lokacija3, lokacija4));

        List<String> mjesta = lokacijaRepository.findDistinctMjesta();

        Assertions.assertEquals(3, mjesta.size());
        Assertions.assertEquals(1, mjesta.stream().filter(m -> m.equals("Zagreb")).count());
    }

    @Test
    public void LokacijaRepository_DeleteExistingLokacija_ReturnLokacijaNotExists() {
        Lokacija lokacija = EntityFactory.createLokacija();

        lokacijaRepository.save(lokacija);
        Long lokacijaId = lokacija.getLokacijaId();

        lokacijaRepository.deleteById(lokacijaId);

        boolean isSavedLokacijaInLokacijaRepository = lokacijaRepository.existsById(lokacijaId);
        Assertions.assertFalse(isSavedLokacijaInLokacijaRepository);
    }

}
