package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Sjedalo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SjedaloRepository extends JpaRepository<Sjedalo, Long> {

    List<Sjedalo> findAllBySektor_SektorIdOrderByRedAscBroj(Long sektorId);

}
