package hr.fer.zpr.infsus.spu.service;

import java.util.List;

import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.dto.SektorFormDto;

public interface SektorService {

	List<Sektor> findAll();

	Sektor getById(Long id);

	Sektor save(SektorFormDto dto);

	Sektor update(Long id, SektorFormDto dto);

	void deleteById(Long id);

	SektorFormDto getFormDtoById(Long id);

	List<Sektor> search(String naziv, Long dvoranaId);

	List<Sektor> findByDvorana(Long dvoranaId);

}
