package hr.fer.zpr.infsus.spu_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import hr.fer.zpr.infsus.spu_backend.model.Cjenik;
import hr.fer.zpr.infsus.spu_backend.repository.CjenikRepository;
import hr.fer.zpr.infsus.spu_backend.service.CjenikService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CjenikServiceImpl implements CjenikService {

	private final CjenikRepository cjenikRepository;

	@Override
	public List<Cjenik> findByDogadaj(Long dogadajId) {

		return cjenikRepository.findAllByDogadaj_DogadajIdOrderByCijena(dogadajId);
	}

	@Override
	public Cjenik save(Cjenik cjenik) {

		return cjenikRepository.save(cjenik);
	}
}