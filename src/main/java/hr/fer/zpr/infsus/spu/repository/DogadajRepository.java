package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Dogadaj;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DogadajRepository extends JpaRepository<Dogadaj, Long> {

	List<Dogadaj> findAllByNazivContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
			@NotBlank @Size(max = 50) String naziv, @NotNull @Future LocalDateTime datumVrijeme);

	List<Dogadaj> findAllByKategorijaContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
			@NotBlank @Size(max = 20) String kategorija, @NotNull @Future LocalDateTime datumVrijeme);

	List<Dogadaj> findAllByNazivContainsIgnoreCaseAndKategorijaContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
			@NotBlank @Size(max = 50) String naziv, @NotBlank @Size(max = 20) String kategorija, @NotNull @Future LocalDateTime datumVrijemeOdrzavanja);

	List<Dogadaj> findAllByOpisContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
			@Size(max = 500) String opis, @NotNull @Future LocalDateTime datumVrijeme);

	List<Dogadaj> findAllByDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
			@NotNull LocalDateTime datumVrijeme);

	List<Dogadaj> findAllByDvorana_DvoranaIdAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
			Long dvorana_dvoranaId, @NotNull @Future LocalDateTime datumVrijeme);

	boolean existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(Long dvoranaId, LocalDateTime datumVrijemeOdrzavanja);

	boolean existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanjaAndDogadajIdNot(Long dvoranaId,
			LocalDateTime datumVrijemeOdrzavanja, Long dogadajId);

}
