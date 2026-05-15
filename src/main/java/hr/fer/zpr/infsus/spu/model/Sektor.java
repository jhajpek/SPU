package hr.fer.zpr.infsus.spu.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sektor")
@Getter
@Setter
@NoArgsConstructor
public class Sektor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sektor_id")
	private Long sektorId;

	@NotBlank
	@Size(max = 20)
	@Column(length = 20, nullable = false)
	private String naziv;

	@NotNull
	@Min(value = 1)
	@Column(nullable = false)
	private Integer kapacitet;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dvorana_id", nullable = false)
	private Dvorana dvorana;

	@OneToMany(mappedBy = "sektor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sjedalo> sjedala = new ArrayList<>();

	@OneToMany(mappedBy = "sektor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cjenik> cjenici = new ArrayList<>();

}
