package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Cjenik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CjenikRepository extends JpaRepository<Cjenik, Long> {

	Optional<Cjenik> findByDogadaj_DogadajIdAndSektor_SektorId(Long dogadajId, Long sektorId);

	List<Cjenik> findAllByDogadaj_DogadajIdOrderByCijena(Long dogadajId);

	boolean existsByDogadaj_DogadajIdAndSektor_SektorId(Long dogadajId, Long sektorId);

	boolean existsBySektor_SektorId(Long sektorId);

}
