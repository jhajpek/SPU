package hr.fer.zpr.infsus.spu_backend.repository;

import hr.fer.zpr.infsus.spu_backend.model.Lokacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LokacijaRepository extends JpaRepository<Lokacija, Long> {

    @Query("SELECT DISTINCT l.mjesto FROM Lokacija l")
    List<String> findDistinctMjesta();

}
