package hr.fer.zpr.infsus.spu_backend.service;

import java.util.List;

import hr.fer.zpr.infsus.spu_backend.model.Cjenik;

public interface CjenikService {

	List<Cjenik> findByDogadaj(Long dogadajId);

	Cjenik save(Cjenik cjenik);

}