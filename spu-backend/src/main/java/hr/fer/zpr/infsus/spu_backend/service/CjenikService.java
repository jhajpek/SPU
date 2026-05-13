package hr.fer.zpr.infsus.spu_backend.service;

import java.util.List;

import hr.fer.zpr.infsus.spu_backend.model.Cjenik;
import hr.fer.zpr.infsus.spu_backend.model.Sektor;
import hr.fer.zpr.infsus.spu_backend.model.dto.CjenikFormDto;

public interface CjenikService {

	List<Cjenik> findByDogadaj(Long dogadajId);

	List<Sektor> findAvailableSectors(Long dogadajId);

	Cjenik save(CjenikFormDto dto);

	Cjenik update(Long id, CjenikFormDto dto);

	void deleteById(Long id);

}