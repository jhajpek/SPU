package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Kupac;
import hr.fer.zpr.infsus.spu.repository.KorisnikRepository;
import hr.fer.zpr.infsus.spu.repository.KupacRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class KupacRepositoryTests {

    @Autowired
    private KorisnikRepository korisnikRepository;
    
    @Autowired
    private KupacRepository kupacRepository;

    @Test
    public void KupacRepository_SaveAndFindKupac_ReturnSavedKupac() {
        Kupac kupac = EntityFactory.createKupac();

        Kupac savedKupac = kupacRepository.save(kupac);
        Assertions.assertNotNull(savedKupac);

        boolean isSavedKupacInKupacRepository = kupacRepository.existsById(savedKupac.getKorisnikId());
        Assertions.assertTrue(isSavedKupacInKupacRepository);

        boolean isSavedKupacInKorisnikRepository = korisnikRepository.existsById(savedKupac.getKorisnikId());
        Assertions.assertTrue(isSavedKupacInKorisnikRepository);
    }

    @Test
    public void KupacRepository_SaveAndFindAllKupci_ReturnKupacList() {
        Kupac kupac1 = EntityFactory.createKupac();
        Kupac kupac2 = EntityFactory.createKupac();
        kupac2.setEmail("marko.mamic2@gmail.com");
        kupacRepository.saveAll(List.of(kupac1, kupac2));

        long numberOfKupaca = kupacRepository.count();
        Assertions.assertEquals(2, numberOfKupaca);

        long numberOfKorisnika = korisnikRepository.count();
        Assertions.assertEquals(2, numberOfKorisnika);
    }

    @Test
    public void KupacRepository_SaveKupacWithInvalidIme_ThrowsException() {
        Kupac kupac = EntityFactory.createKupac();
        kupac.setIme("");
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> kupacRepository.save(kupac)
        );
    }

    @Test
    public void KupacRepository_SaveDuplicatedKupac_ThrowsException() {
        Kupac kupac1 = EntityFactory.createKupac();
        kupacRepository.save(kupac1);

        Kupac kupac2 = EntityFactory.createKupac();

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> kupacRepository.save(kupac2)
        );
    }

    @Test
    public void KupacRepository_DeleteExistingKupac_ReturnKupacNotExists() {
        Kupac kupac = EntityFactory.createKupac();

        kupacRepository.save(kupac);
        Long kupacId = kupac.getKorisnikId();

        kupacRepository.deleteById(kupacId);

        boolean isSavedKupacInKupacRepository = kupacRepository.existsById(kupacId);
        Assertions.assertFalse(isSavedKupacInKupacRepository);

        boolean isSavedKupacInKorisnikRepository = korisnikRepository.existsById(kupacId);
        Assertions.assertFalse(isSavedKupacInKorisnikRepository);
    }

}
