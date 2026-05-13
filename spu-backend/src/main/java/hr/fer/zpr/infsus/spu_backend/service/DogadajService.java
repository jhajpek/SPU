package hr.fer.zpr.infsus.spu_backend.service;

import java.util.List;
import java.util.Optional;

import hr.fer.zpr.infsus.spu_backend.model.Dogadaj;
import hr.fer.zpr.infsus.spu_backend.model.dto.DogadajFormDto;

public interface DogadajService {

	List<Dogadaj> findAll();

	Optional<Dogadaj> findById(Long id);

	Dogadaj save(DogadajFormDto dogadaj);

	void deleteById(Long id);

	Dogadaj update(Long id, DogadajFormDto dto);

	List<Dogadaj> search(String naziv, String kategorija);

	DogadajFormDto getFormDtoById(Long id);

}