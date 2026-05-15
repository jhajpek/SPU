package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {

	boolean existsByDogadaj_DogadajIdAndSjedalo_SjedaloIdAndDatumVrijemeIstekaAfter(Long dogadajId, Long sjedaloId,
			LocalDateTime datumVrijeme);

	List<Rezervacija> findAllByKupac_KorisnikIdAndDatumVrijemeIstekaAfter(Long kupacId, LocalDateTime datumVrijeme);

	void deleteAllByDatumVrijemeIstekaBefore(LocalDateTime datumVrijeme);

	boolean existsByDogadaj_DogadajId(Long dogadajId);

}
