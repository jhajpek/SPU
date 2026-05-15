package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
}
