package hr.fer.zpr.infsus.spu_backend.repository;

import hr.fer.zpr.infsus.spu_backend.model.Kupac;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KupacRepository extends JpaRepository<Kupac, Long> {
}
