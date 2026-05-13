package hr.fer.zpr.infsus.spu_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import hr.fer.zpr.infsus.spu_backend.model.Dogadaj;
import hr.fer.zpr.infsus.spu_backend.model.Dvorana;
import hr.fer.zpr.infsus.spu_backend.model.dto.DogadajFormDto;
import hr.fer.zpr.infsus.spu_backend.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu_backend.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu_backend.repository.RezervacijaRepository;
import hr.fer.zpr.infsus.spu_backend.repository.UlaznicaRepository;
import hr.fer.zpr.infsus.spu_backend.service.DogadajService;

@Service
@RequiredArgsConstructor
public class DogadajServiceImpl implements DogadajService {

	private final DogadajRepository dogadajRepository;
	private final DvoranaRepository dvoranaRepository;
	private final UlaznicaRepository ulaznicaRepository;
	private final RezervacijaRepository rezervacijaRepository;

	@Override
	public List<Dogadaj> findAll() {
		return dogadajRepository.findAll();
	}

	@Override
	public Optional<Dogadaj> findById(Long id) {
		return dogadajRepository.findById(id);
	}

	@Override
	public Dogadaj save(DogadajFormDto dto) {

		if (dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(dto.getDvoranaId(),
				dto.getDatumVrijemeOdrzavanja())) {
			throw new IllegalArgumentException("U odabranoj dvorani već postoji događaj u tom terminu.");
		}

		if (dto.getDatumVrijemeOdrzavanja().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Datum događaja mora biti u budućnosti.");
		}

		Dvorana dvorana = dvoranaRepository.findById(dto.getDvoranaId()).orElseThrow();
		Dogadaj dogadaj = new Dogadaj();

		dogadaj.setNaziv(dto.getNaziv());
		dogadaj.setKategorija(dto.getKategorija());
		dogadaj.setOpis(dto.getOpis());
		dogadaj.setDatumVrijemeOdrzavanja(dto.getDatumVrijemeOdrzavanja());
		dogadaj.setDvorana(dvorana);

		return dogadajRepository.save(dogadaj);
	}

	@Override
	public void deleteById(Long id) {
		try {
			dogadajRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException(
					"Događaj nije moguće obrisati jer postoje povezane rezervacije ili ulaznice.");
		}
	}

	@Override
	public Dogadaj update(Long id, DogadajFormDto dto) {

		if (dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanjaAndDogadajIdNot(dto.getDvoranaId(),
				dto.getDatumVrijemeOdrzavanja(), id)) {
			throw new IllegalArgumentException("U odabranoj dvorani već postoji događaj u tom terminu.");
		}

		Dogadaj dogadaj = dogadajRepository.findById(id).orElseThrow();

		if (dto.getDatumVrijemeOdrzavanja().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Datum događaja mora biti u budućnosti.");
		}

		Dvorana dvorana = dvoranaRepository.findById(dto.getDvoranaId()).orElseThrow();

		dogadaj.setNaziv(dto.getNaziv());
		dogadaj.setKategorija(dto.getKategorija());
		dogadaj.setOpis(dto.getOpis());
		dogadaj.setDatumVrijemeOdrzavanja(dto.getDatumVrijemeOdrzavanja());

		boolean mijenjaDvoranu = !dogadaj.getDvorana().getDvoranaId().equals(dto.getDvoranaId());

		boolean postojeUlaznice = ulaznicaRepository.existsByDogadaj_DogadajId(id);

		boolean postojeRezervacije = rezervacijaRepository.existsByDogadaj_DogadajId(id);

		if (mijenjaDvoranu && (postojeUlaznice || postojeRezervacije)) {

			throw new IllegalArgumentException(
					"Dvoranu nije moguće promijeniti " + "jer postoje prodane ulaznice ili rezervacije.");
		}

		dogadaj.setDvorana(dvorana);

		return dogadajRepository.save(dogadaj);
	}

	@Override
	public List<Dogadaj> search(String naziv, String kategorija) {

		naziv = naziv == null ? "" : naziv.trim();
		kategorija = kategorija == null ? "" : kategorija.trim();

		if (!naziv.isEmpty()) {
			return dogadajRepository
					.findAllByNazivContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(naziv,
							LocalDateTime.now());
		}

		if (!kategorija.isEmpty()) {
			return dogadajRepository
					.findAllByKategorijaContainsIgnoreCaseAndDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(
							kategorija, LocalDateTime.now());
		}
		return dogadajRepository.findAllByDatumVrijemeOdrzavanjaAfterOrderByDatumVrijemeOdrzavanja(LocalDateTime.now());
	}

	@Override
	public DogadajFormDto getFormDtoById(Long id) {

		Dogadaj dogadaj = dogadajRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Događaj ne postoji."));

		DogadajFormDto dto = new DogadajFormDto();

		dto.setDogadajId(dogadaj.getDogadajId());
		dto.setNaziv(dogadaj.getNaziv());
		dto.setKategorija(dogadaj.getKategorija());
		dto.setOpis(dogadaj.getOpis());

		dto.setDatumVrijemeOdrzavanja(dogadaj.getDatumVrijemeOdrzavanja());

		dto.setDvoranaId(dogadaj.getDvorana().getDvoranaId());

		return dto;
	}

}