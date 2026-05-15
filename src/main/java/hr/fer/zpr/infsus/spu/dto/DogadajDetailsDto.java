package hr.fer.zpr.infsus.spu.dto;

import java.util.List;
import java.util.Map;

import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Sektor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DogadajDetailsDto {
	
	private Dogadaj dogadaj;

	private List<Cjenik> cjenici;

	private Map<Long, Long> prodaneUlaznice;

	private List<Sektor> dostupniSektori;

	private CjenikFormDto noviCjenik;
	
}
