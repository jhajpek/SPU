package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    boolean existsByEmail(String email);
    
}
