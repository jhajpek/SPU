package hr.fer.zpr.infsus.spu_backend.repository;

import hr.fer.zpr.infsus.spu_backend.model.Cjenik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CjenikRepository extends JpaRepository<Cjenik, Long> {

    Optional<Cjenik> findByDogadaj_DogadajIdAndSektor_SektorId(Long dogadajId, Long sektorId);

    List<Cjenik> findAllByDogadaj_DogadajIdOrderByCijena(Long dogadajId);

}
