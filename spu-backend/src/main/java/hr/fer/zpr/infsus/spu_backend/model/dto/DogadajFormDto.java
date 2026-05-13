package hr.fer.zpr.infsus.spu_backend.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DogadajFormDto {

	private Long dogadajId;

	@NotBlank
	@Size(max = 50)
	private String naziv;

	@NotBlank
	@Size(max = 20)
	private String kategorija;

	@Size(max = 500)
	private String opis;

	@NotNull
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime datumVrijemeOdrzavanja;

	@NotNull
	private Long dvoranaId;

	@NotNull
	private Map<Long, BigDecimal> cijenePoSektorima = new HashMap<>();
}