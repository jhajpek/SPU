package hr.fer.zpr.infsus.spu_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import hr.fer.zpr.infsus.spu_backend.model.Lokacija;
import hr.fer.zpr.infsus.spu_backend.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu_backend.service.LokacijaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LokacijaServiceImpl implements LokacijaService {

	private final LokacijaRepository lokacijaRepository;

	@Override
	public List<Lokacija> findAll() {
		return lokacijaRepository.findAll();
	}
}
