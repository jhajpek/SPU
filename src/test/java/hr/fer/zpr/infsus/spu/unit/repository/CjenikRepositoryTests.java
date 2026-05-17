package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.repository.CjenikRepository;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
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

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class CjenikRepositoryTests {

    @Autowired
    private LokacijaRepository lokacijaRepository;

    @Autowired
    private DvoranaRepository dvoranaRepository;

    @Autowired
    private SektorRepository sektorRepository;

    @Autowired
    private DogadajRepository dogadajRepository;

    @Autowired
    private CjenikRepository cjenikRepository;

    private Dogadaj testDogadaj1;
    
    private Dogadaj testDogadaj2;

    private Sektor testSektor1;

    private Sektor testSektor2;

    @BeforeEach
    public void setUpTestEntities() {
        Lokacija lokacija = lokacijaRepository.save(EntityFactory.createLokacija());
        Dvorana dvorana = dvoranaRepository.save(EntityFactory.createDvorana("Arena", lokacija));
        testDogadaj1 = dogadajRepository.save(EntityFactory.createDogadaj("Dogadaj1", dvorana));
        testDogadaj2 = dogadajRepository.save(EntityFactory.createDogadaj("Dogadaj2", dvorana));
        testSektor1 = sektorRepository.save(EntityFactory.createSektor("Jug", 100, dvorana));
        testSektor2 = sektorRepository.save(EntityFactory.createSektor("Istok", 100, dvorana));
    }

    @Test
    public void CjenikRepository_FindAllCjeniciByDogadajId_ReturnSortedCjenikList() {
        cjenikRepository.save(EntityFactory.createCjenik(new BigDecimal("35.00"), testDogadaj1, testSektor1));
        cjenikRepository.save(EntityFactory.createCjenik(new BigDecimal("30.00"), testDogadaj1, testSektor2));
        
        List<Cjenik> cjenici = cjenikRepository.findAllByDogadaj_DogadajIdOrderByCijena(testDogadaj1.getDogadajId());
        
        Assertions.assertEquals(2, cjenici.size());
        Assertions.assertEquals(new BigDecimal("30.00"), cjenici.get(0).getCijena());
        Assertions.assertEquals(new BigDecimal("35.00"), cjenici.get(1).getCijena());
    }

    @Test
    public void CjenikRepository_FindCjenikByDogadajAndSektor_ReturnCjenikExists() {
        cjenikRepository.save(EntityFactory.createCjenik(new BigDecimal("30.00"), testDogadaj1, testSektor1));

        boolean existsExample = cjenikRepository.existsByDogadaj_DogadajIdAndSektor_SektorId(
                testDogadaj1.getDogadajId(), testSektor1.getSektorId()
        );

        boolean notExistsExample1 = cjenikRepository.existsByDogadaj_DogadajIdAndSektor_SektorId(
                testDogadaj1.getDogadajId(), testSektor2.getSektorId()
        );

        boolean notExistsExample2 = cjenikRepository.existsByDogadaj_DogadajIdAndSektor_SektorId(
                testDogadaj2.getDogadajId(), testSektor1.getSektorId()
        );
        
        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample1);
        Assertions.assertFalse(notExistsExample2);
    }

    @Test
    public void CjenikRepository_FindCjenikBySektorId_ReturnsCjenikExists() {
        cjenikRepository.save(EntityFactory.createCjenik(new BigDecimal("30.00"), testDogadaj1, testSektor1));
        
        boolean existsExample = cjenikRepository.existsBySektor_SektorId(testSektor1.getSektorId());
        boolean notExistsExample = cjenikRepository.existsBySektor_SektorId(testSektor2.getSektorId());
        
        Assertions.assertTrue(existsExample);
        Assertions.assertFalse(notExistsExample);
    }

    @Test
    public void CjenikRepository_SaveCjenikWithInvalidCijena_ThrowsException() {
        Cjenik cjenik = EntityFactory.createCjenik(new BigDecimal("-30.00"), testDogadaj1, testSektor1);
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> cjenikRepository.save(cjenik)
        );
    }

    @Test
    public void CjenikRepository_DeleteExistingCjenik_ReturnDogadajAndSektorExists() {
        Long cjenikId = cjenikRepository.save(
                EntityFactory.createCjenik(new BigDecimal("30.00"), testDogadaj1, testSektor1)
        ).getCjenikId();

        cjenikRepository.deleteById(cjenikId);

        boolean cjenikNotExists = cjenikRepository.existsById(cjenikId);
        boolean dogadajExists = dogadajRepository.existsById(testDogadaj1.getDogadajId());
        boolean sektorExists = sektorRepository.existsById(testSektor1.getSektorId());

        Assertions.assertFalse(cjenikNotExists);
        Assertions.assertTrue(dogadajExists);
        Assertions.assertTrue(sektorExists);
    }

}
