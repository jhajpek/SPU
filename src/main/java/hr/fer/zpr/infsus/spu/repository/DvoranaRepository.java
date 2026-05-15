package hr.fer.zpr.infsus.spu.repository;

import hr.fer.zpr.infsus.spu.model.Dvorana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DvoranaRepository extends JpaRepository<Dvorana, Long> {

	List<Dvorana> findAllByNazivContainsIgnoreCase(String naziv);

	List<Dvorana> findAllByNazivContainsIgnoreCaseAndLokacija_Mjesto(String naziv, String mjesto);

	List<Dvorana> findAllByNazivContainsIgnoreCaseAndLokacija_MjestoContainsIgnoreCase(String naziv, String mjesto);

	List<Dvorana> findAllByLokacija_MjestoContainsIgnoreCase(String mjesto);

	boolean existsByNazivIgnoreCaseAndLokacija_LokacijaId(String naziv, Long lokacijaId);

	boolean existsByNazivIgnoreCaseAndLokacija_LokacijaIdAndDvoranaIdNot(String naziv, Long lokacijaId, Long dvoranaId);

	@Query("""
			    SELECT d
			    FROM Dvorana d
			    WHERE LOWER(d.naziv) LIKE LOWER(CONCAT('%', :query, '%'))
			       OR LOWER(d.lokacija.ulica) LIKE LOWER(CONCAT('%', :query, '%'))
			       OR LOWER(d.lokacija.mjesto) LIKE LOWER(CONCAT('%', :query, '%'))
			       OR LOWER(d.lokacija.postanskiBroj) LIKE LOWER(CONCAT('%', :query, '%'))
			       OR CAST(d.lokacija.kucniBroj AS string)
			            LIKE CONCAT('%', :query, '%')
			""")
	List<Dvorana> searchLocation(String query);

}
