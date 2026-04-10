package hr.fer.zpr.infsus.spu_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "dogadaj")
@Getter
@Setter
@NoArgsConstructor
public class Dogadaj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dogadaj_id")
    private Long dogadajId;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String naziv;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String kategorija;

    @Size(max = 500)
    @Column(length = 500)
    private String opis;

    @NotNull
    @Future
    @Column(nullable = false)
    private LocalDateTime datumVrijemeOdrzavanja;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dvorana_id", nullable = false)
    private Dvorana dvorana;

}
