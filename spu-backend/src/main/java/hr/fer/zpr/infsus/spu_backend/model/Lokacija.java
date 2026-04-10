package hr.fer.zpr.infsus.spu_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lokacija")
@Getter
@Setter
@NoArgsConstructor
public class Lokacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lokacija_id")
    private Long lokacijaId;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String ulica;

    @NotNull
    @Min(value = 1)
    @Column(name = "kucni_broj", nullable = false)
    private Integer kucniBroj;

    @NotBlank
    @Size(min = 5, max = 5)
    @Pattern(regexp = "\\d{5}")
    @Column(name = "postanski_broj", length = 5, nullable = false)
    private String postanskiBroj;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String mjesto;

}
