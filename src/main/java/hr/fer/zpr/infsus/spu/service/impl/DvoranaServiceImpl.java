package hr.fer.zpr.infsus.spu.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.dto.DvoranaFormDto;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DvoranaServiceImpl implements DvoranaService {

	private final DvoranaRepository dvoranaRepository;
	private final LokacijaRepository lokacijaRepository;

	@Override
	public List<Dvorana> findAll() {
		return dvoranaRepository.findAll();
	}

	@Override
	public Optional<Dvorana> findById(Long id) {
		return dvoranaRepository.findById(id);
	}

	@Override
	public Dvorana save(DvoranaFormDto dto) {

		if (dvoranaRepository.existsByNazivIgnoreCaseAndLokacija_LokacijaId(dto.getNaziv(), dto.getLokacijaId())) {

			throw new IllegalArgumentException("Dvorana s tim nazivom već postoji na odabranoj lokaciji.");
		}

		Lokacija lokacija = lokacijaRepository.findById(dto.getLokacijaId())
				.orElseThrow(() -> new IllegalArgumentException("Lokacija ne postoji."));
		Dvorana dvorana = new Dvorana();
		dvorana.setNaziv(dto.getNaziv());
		dvorana.setLokacija(lokacija);

		return dvoranaRepository.save(dvorana);
	}

	@Override
	public Dvorana update(Long id, DvoranaFormDto dto) {

		if (dvoranaRepository.existsByNazivIgnoreCaseAndLokacija_LokacijaIdAndDvoranaIdNot(dto.getNaziv(),
				dto.getLokacijaId(), id)) {

			throw new IllegalArgumentException("Dvorana s tim nazivom već postoji na odabranoj lokaciji.");
		}

		Dvorana dvorana = dvoranaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Dvorana ne postoji."));
		Lokacija lokacija = lokacijaRepository.findById(dto.getLokacijaId())
				.orElseThrow(() -> new IllegalArgumentException("Lokacija ne postoji."));
		dvorana.setNaziv(dto.getNaziv());
		dvorana.setLokacija(lokacija);

		return dvoranaRepository.save(dvorana);
	}

	@Override
	public void deleteById(Long id) {
		try {
			dvoranaRepository.deleteById(id);
		} catch (InvalidDataAccessApiUsageException | DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Dvoranu nije moguće obrisati jer postoje događaji vezani uz nju.");
		}
	}

	@Override
	public DvoranaFormDto getFormDtoById(Long id) {

		Dvorana dvorana = dvoranaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Dvorana ne postoji."));

		DvoranaFormDto dto = new DvoranaFormDto();

		dto.setDvoranaId(dvorana.getDvoranaId());
		dto.setNaziv(dvorana.getNaziv());
		dto.setLokacijaId(dvorana.getLokacija().getLokacijaId());

		return dto;
	}

	@Override
	public List<Dvorana> search(String query) {

		query = query == null ? "" : query.trim();

		if (query.isEmpty()) {
			return dvoranaRepository.findAll();
		}

		return dvoranaRepository.searchLocation(query);
	}

}
