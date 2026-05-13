package hr.fer.zpr.infsus.spu_backend.service;

import java.util.List;
import java.util.Optional;

import hr.fer.zpr.infsus.spu_backend.model.Dvorana;
import hr.fer.zpr.infsus.spu_backend.model.dto.DvoranaFormDto;

public interface DvoranaService {

	List<Dvorana> findAll();

	Optional<Dvorana> findById(Long id);

	Dvorana save(DvoranaFormDto dto);

	Dvorana update(Long id, DvoranaFormDto dto);

	void deleteById(Long id);

	DvoranaFormDto getFormDtoById(Long id);

	List<Dvorana> search(String query);

}