package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Ulaznica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UlaznicaRepository extends JpaRepository<Ulaznica, Long> {

	Optional<Ulaznica> findByQrKod(String qrKod);

	boolean existsByDogadaj_DogadajIdAndSjedalo_SjedaloId(Long dogadajId, Long sjedaloId);

	List<Ulaznica> findAllByKupac_KorisnikIdOrderByDatumVrijemeKupnjeDesc(Long kupacId);

	List<Ulaznica> findAllByDogadaj_DogadajId(Long dogadajId);

	long countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(Long dogadajId, Long sektorId);

	boolean existsByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(Long dogadajId, Long sektorId);

	boolean existsBySjedalo_Sektor_SektorId(Long sektorId);

	boolean existsByDogadaj_DogadajId(Long dogadajId);

}
