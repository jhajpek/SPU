package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    boolean existsByEmail(String email);

    Optional<Korisnik> findByEmail(String email);

}
