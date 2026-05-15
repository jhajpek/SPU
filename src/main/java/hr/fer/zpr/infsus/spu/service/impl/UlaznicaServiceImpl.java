package hr.fer.zpr.infsus.spu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.repository.CjenikRepository;
import hr.fer.zpr.infsus.spu.repository.UlaznicaRepository;
import hr.fer.zpr.infsus.spu.service.UlaznicaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UlaznicaServiceImpl implements UlaznicaService {

	private final UlaznicaRepository ulaznicaRepository;
	private final CjenikRepository cjenikRepository;

	@Override
	public long countSoldTickets(Long dogadajId, Long sektorId) {

		return ulaznicaRepository.countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(dogadajId, sektorId);
	}

	@Override
	public Map<Long, Long> getSoldTicketsBySektor(Long dogadajId) {

		Map<Long, Long> prodaneUlaznice = new HashMap<>();
		List<Cjenik> cjenici = cjenikRepository.findAllByDogadaj_DogadajIdOrderByCijena(dogadajId);

		for (Cjenik cjenik : cjenici) {
			long broj = ulaznicaRepository.countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(dogadajId,
					cjenik.getSektor().getSektorId());
			prodaneUlaznice.put(cjenik.getSektor().getSektorId(), broj);
		}

		return prodaneUlaznice;
	}

}
