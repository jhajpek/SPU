package hr.fer.zpr.infsus.spu_backend.repository;

import hr.fer.zpr.infsus.spu_backend.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
}
