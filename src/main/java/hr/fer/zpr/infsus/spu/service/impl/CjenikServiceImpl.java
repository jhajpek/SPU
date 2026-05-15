package hr.fer.zpr.infsus.spu.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.dto.CjenikFormDto;
import hr.fer.zpr.infsus.spu.repository.CjenikRepository;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.SektorRepository;
import hr.fer.zpr.infsus.spu.repository.UlaznicaRepository;
import hr.fer.zpr.infsus.spu.service.CjenikService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CjenikServiceImpl implements CjenikService {

	private final CjenikRepository cjenikRepository;
	private final DogadajRepository dogadajRepository;
	private final SektorRepository sektorRepository;
	private final UlaznicaRepository ulaznicaRepository;

	@Override
	public List<Cjenik> findByDogadaj(Long dogadajId) {

		return cjenikRepository.findAllByDogadaj_DogadajIdOrderByCijena(dogadajId);
	}

	@Override
	public List<Sektor> findAvailableSectors(Long dogadajId) {

		Dogadaj dogadaj = dogadajRepository.findById(dogadajId)
				.orElseThrow(() -> new IllegalArgumentException("Događaj ne postoji."));

		List<Sektor> sviSektori = sektorRepository
				.findAllByDvorana_DvoranaIdOrderByNaziv(dogadaj.getDvorana().getDvoranaId());

		List<Long> vecDodaniSektori = dogadaj.getCjenici().stream().map(c -> c.getSektor().getSektorId()).toList();

		return sviSektori.stream().filter(sektor -> !vecDodaniSektori.contains(sektor.getSektorId())).toList();
	}

	@Override
	public Cjenik save(CjenikFormDto dto) {

		if (cjenikRepository.existsByDogadaj_DogadajIdAndSektor_SektorId(dto.getDogadajId(), dto.getSektorId())) {

			throw new IllegalArgumentException("Sektor je već dodan za ovaj događaj.");
		}

		Dogadaj dogadaj = dogadajRepository.findById(dto.getDogadajId())
				.orElseThrow(() -> new IllegalArgumentException("Događaj ne postoji."));

		Sektor sektor = sektorRepository.findById(dto.getSektorId())
				.orElseThrow(() -> new IllegalArgumentException("Sektor ne postoji."));

		if (!sektor.getDvorana().getDvoranaId().equals(dogadaj.getDvorana().getDvoranaId())) {

			throw new IllegalArgumentException("Sektor ne pripada odabranoj dvorani.");
		}

		Cjenik cjenik = new Cjenik();

		cjenik.setDogadaj(dogadaj);
		cjenik.setSektor(sektor);
		cjenik.setCijena(dto.getCijena());

		return cjenikRepository.save(cjenik);
	}

	@Override
	public Cjenik update(Long id, CjenikFormDto dto) {

		Cjenik cjenik = cjenikRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Cjenik ne postoji."));

		cjenik.setCijena(dto.getCijena());

		return cjenikRepository.save(cjenik);
	}

	@Override
	public void deleteById(Long id) {

		Cjenik cjenik = cjenikRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Cjenik ne postoji."));

		boolean postojeUlaznice = ulaznicaRepository.existsByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(
				cjenik.getDogadaj().getDogadajId(), cjenik.getSektor().getSektorId());

		if (postojeUlaznice) {

			throw new IllegalArgumentException("Sektor nije moguće ukloniti jer postoje prodane ulaznice.");
		}

		cjenikRepository.delete(cjenik);
	}

}
