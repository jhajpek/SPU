package hr.fer.zpr.infsus.spu_backend.repository;

import hr.fer.zpr.infsus.spu_backend.model.Ulaznica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UlaznicaRepository extends JpaRepository<Ulaznica, Long> {

    Optional<Ulaznica> findByQrKod(String qrKod);

    boolean existsByDogadaj_DogadajIdAndSjedalo_SjedaloId(Long dogadajId, Long sjedaloId);

    List<Ulaznica> findAllByKupac_KorisnikIdOrderByDatumVrijemeKupnjeDesc(Long kupacId);

    List<Ulaznica> findAllByDogadaj_DogadajId(Long dogadajId);

}
