package hr.fer.zpr.infsus.spu_backend.service.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import hr.fer.zpr.infsus.spu_backend.model.Dvorana;
import hr.fer.zpr.infsus.spu_backend.model.Sektor;
import hr.fer.zpr.infsus.spu_backend.model.dto.SektorFormDto;
import hr.fer.zpr.infsus.spu_backend.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu_backend.repository.SektorRepository;
import hr.fer.zpr.infsus.spu_backend.service.SektorService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SektorServiceImpl implements SektorService {

	private final SektorRepository sektorRepository;
	private final DvoranaRepository dvoranaRepository;

	@Override
	public List<Sektor> findAll() {

		return sektorRepository.findAll();
	}

	@Override
	public Sektor getById(Long id) {

		return sektorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sektor ne postoji."));
	}

	@Override
	public Sektor save(SektorFormDto dto) {

		if (sektorRepository.existsByNazivIgnoreCaseAndDvorana_DvoranaId(dto.getNaziv(), dto.getDvoranaId())) {

			throw new IllegalArgumentException("Sektor s tim nazivom već postoji u odabranoj dvorani.");
		}

		Dvorana dvorana = dvoranaRepository.findById(dto.getDvoranaId())
				.orElseThrow(() -> new IllegalArgumentException("Dvorana ne postoji."));

		Sektor sektor = new Sektor();

		sektor.setNaziv(dto.getNaziv());
		sektor.setKapacitet(dto.getKapacitet());
		sektor.setDvorana(dvorana);

		return sektorRepository.save(sektor);
	}

	@Override
	public Sektor update(Long id, SektorFormDto dto) {

		if (sektorRepository.existsByNazivIgnoreCaseAndDvorana_DvoranaIdAndSektorIdNot(dto.getNaziv(),
				dto.getDvoranaId(), id)) {

			throw new IllegalArgumentException("Sektor s tim nazivom već postoji u odabranoj dvorani.");
		}

		Sektor sektor = sektorRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Sektor ne postoji."));

		Dvorana dvorana = dvoranaRepository.findById(dto.getDvoranaId())
				.orElseThrow(() -> new IllegalArgumentException("Dvorana ne postoji."));

		sektor.setNaziv(dto.getNaziv());
		sektor.setKapacitet(dto.getKapacitet());
		sektor.setDvorana(dvorana);

		return sektorRepository.save(sektor);
	}

	@Override
	public void deleteById(Long id) {
		try {
			sektorRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException(
					"Sektor nije moguće obrisati jer postoje povezane rezervacije ili ulaznice.");
		}
	}

	@Override
	public SektorFormDto getFormDtoById(Long id) {

		Sektor sektor = sektorRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Sektor ne postoji."));

		SektorFormDto dto = new SektorFormDto();

		dto.setSektorId(sektor.getSektorId());
		dto.setNaziv(sektor.getNaziv());
		dto.setKapacitet(sektor.getKapacitet());
		dto.setDvoranaId(sektor.getDvorana().getDvoranaId());

		return dto;
	}

	@Override
	public List<Sektor> search(String naziv, Long dvoranaId) {

		naziv = naziv == null ? "" : naziv.trim();

		if (!naziv.isEmpty() && dvoranaId != null) {
			return sektorRepository.findAllByNazivContainsIgnoreCaseAndDvorana_DvoranaIdOrderByNaziv(naziv, dvoranaId);
		}

		if (!naziv.isEmpty()) {
			return sektorRepository.findAllByNazivContainsIgnoreCaseOrderByDvorana_Naziv(naziv);
		}

		if (dvoranaId != null) {
			return sektorRepository.findAllByDvorana_DvoranaIdOrderByNaziv(dvoranaId);
		}

		return sektorRepository.findAll();
	}

	@Override
	public List<Sektor> findByDvorana(Long dvoranaId) {

		return sektorRepository.findAllByDvorana_DvoranaIdOrderByNaziv(dvoranaId);
	}
}