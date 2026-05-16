package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Sektor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SektorRepository extends JpaRepository<Sektor, Long> {

	List<Sektor> findAllByNazivContainsIgnoreCaseOrderByDvorana_Naziv(String naziv);

	List<Sektor> findAllByDvorana_DvoranaIdOrderByNaziv(Long dvoranaId);

	List<Sektor> findAllByNazivContainsIgnoreCaseAndDvorana_DvoranaIdOrderByNaziv(String naziv, Long dvoranaId);

	boolean existsByNazivIgnoreCaseAndDvorana_DvoranaId(String naziv, Long dvoranaId);

	boolean existsByNazivIgnoreCaseAndDvorana_DvoranaIdAndSektorIdNot(String naziv, Long dvoranaId, Long sektorId);

}
