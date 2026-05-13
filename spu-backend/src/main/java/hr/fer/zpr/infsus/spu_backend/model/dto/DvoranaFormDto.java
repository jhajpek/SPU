package hr.fer.zpr.infsus.spu_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DvoranaFormDto {

	private Long dvoranaId;

	@NotBlank
	@Size(max = 50)
	private String naziv;

	@NotNull
	private Long lokacijaId;

}
