package hr.fer.zpr.infsus.spu.unit.repository;

import hr.fer.zpr.infsus.spu.model.Administrator;
import hr.fer.zpr.infsus.spu.repository.AdministratorRepository;
import hr.fer.zpr.infsus.spu.repository.KorisnikRepository;
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
public class AdministratorRepositoryTests {

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Test
    public void AdministratorRepository_SaveAndFindAdministrator_ReturnSavedAdministrator() {
        Administrator administrator = EntityFactory.createAdministrator();

        Administrator savedAdmin = administratorRepository.save(administrator);
        Assertions.assertNotNull(savedAdmin);

        boolean isSavedAdminInAdministratorRepository = administratorRepository.existsById(savedAdmin.getKorisnikId());
        Assertions.assertTrue(isSavedAdminInAdministratorRepository);

        boolean isSavedAdminInKorisnikRepository = korisnikRepository.existsById(savedAdmin.getKorisnikId());
        Assertions.assertTrue(isSavedAdminInKorisnikRepository);
    }

    @Test
    public void AdministratorRepository_SaveAndFindAllAdministratori_ReturnAdministratorList() {
        Administrator administrator1 = EntityFactory.createAdministrator();
        Administrator administrator2 = EntityFactory.createAdministrator();
        administrator2.setEmail("marko.mamic2@gmail.com");
        administratorRepository.saveAll(List.of(administrator1, administrator2));

        long numberOfAdministratora = administratorRepository.count();
        Assertions.assertEquals(2, numberOfAdministratora);

        long numberOfKorisnika = korisnikRepository.count();
        Assertions.assertEquals(2, numberOfKorisnika);
    }

    @Test
    public void AdministratorRepository_SaveAdministratorWithInvalidIme_ThrowsException() {
        Administrator administrator = EntityFactory.createAdministrator();
        administrator.setIme("");
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> administratorRepository.save(administrator)
        );
    }

    @Test
    public void AdministratorRepository_SaveDuplicatedAdministrator_ThrowsException() {
        Administrator administrator1 = EntityFactory.createAdministrator();
        administratorRepository.save(administrator1);

        Administrator administrator2 = EntityFactory.createAdministrator();

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> administratorRepository.save(administrator2)
        );
    }

    @Test
    public void AdministratorRepository_DeleteExistingAdministrator_ReturnAdministratorNotExists() {
        Administrator administrator = EntityFactory.createAdministrator();

        administratorRepository.save(administrator);
        Long administratorId = administrator.getKorisnikId();

        administratorRepository.deleteById(administratorId);

        boolean isSavedAdminInAdministratorRepository = administratorRepository.existsById(administratorId);
        Assertions.assertFalse(isSavedAdminInAdministratorRepository);

        boolean isSavedAdminInKorisnikRepository = korisnikRepository.existsById(administratorId);
        Assertions.assertFalse(isSavedAdminInKorisnikRepository);
    }

}
