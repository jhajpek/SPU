package hr.fer.zpr.infsus.spu.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CjenikFormDto {

	@NotNull
	private Long dogadajId;

	@NotNull
	private Long sektorId;

	@NotNull
	@DecimalMin(value = "0.00")
	private BigDecimal cijena;

}
