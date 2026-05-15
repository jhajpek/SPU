package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Kupac;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KupacRepository extends JpaRepository<Kupac, Long> {
}
