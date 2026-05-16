package hr.fer.zpr.infsus.spu.service;

import java.util.List;
import java.util.Optional;

import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.dto.DvoranaFormDto;
import hr.fer.zpr.infsus.spu.model.Lokacija;

public interface DvoranaService {

	List<Dvorana> findAll();

	List<Lokacija> findAllUnusedLokacije();

	Optional<Dvorana> findById(Long id);

	Dvorana save(DvoranaFormDto dto);

	Dvorana update(Long id, DvoranaFormDto dto);

	void deleteById(Long id);

	DvoranaFormDto getFormDtoById(Long id);

	List<Dvorana> search(String query);

}
