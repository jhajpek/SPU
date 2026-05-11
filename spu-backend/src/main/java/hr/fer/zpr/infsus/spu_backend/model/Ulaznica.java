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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "ulaznica",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_dogadaj_sjedalo",
                        columnNames = {"dogadaj_id", "sjedalo_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Ulaznica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ulaznica_id")
    private Long ulaznicaId;

    @NotBlank
    @Size(max = 500)
    @Column(name = "qr_kod", unique = true, length = 500, nullable = false)
    private String qrKod;

    @NotNull
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime datumVrijemeKupnje;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Kupac kupac;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dogadaj_id", nullable = false)
    private Dogadaj dogadaj;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sjedalo_id", nullable = false)
    private Sjedalo sjedalo;

}
