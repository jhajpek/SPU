package hr.fer.zpr.infsus.spu_backend.repository;

import hr.fer.zpr.infsus.spu_backend.model.Dvorana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DvoranaRepository extends JpaRepository<Dvorana, Long> {

    List<Dvorana> findAllByNazivContainsIgnoreCase(String naziv);

    List<Dvorana> findAllByNazivContainsIgnoreCaseAndLokacija_Mjesto(String naziv, String mjesto);

}
