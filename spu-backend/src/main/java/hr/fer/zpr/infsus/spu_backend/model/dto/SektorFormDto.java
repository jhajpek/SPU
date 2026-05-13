package hr.fer.zpr.infsus.spu_backend.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SektorFormDto {

	private Long sektorId;

	@NotBlank
	@Size(max = 20)
	private String naziv;

	@NotNull
	@Min(1)
	private Integer kapacitet;

	@NotNull
	private Long dvoranaId;
}