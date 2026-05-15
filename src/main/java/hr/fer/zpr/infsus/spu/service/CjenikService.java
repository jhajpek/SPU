package hr.fer.zpr.infsus.spu.service;

import java.util.List;

import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.dto.CjenikFormDto;

public interface CjenikService {

	List<Cjenik> findByDogadaj(Long dogadajId);

	List<Sektor> findAvailableSectors(Long dogadajId);

	Cjenik save(CjenikFormDto dto);

	Cjenik update(Long id, CjenikFormDto dto);

	void deleteById(Long id);

}
