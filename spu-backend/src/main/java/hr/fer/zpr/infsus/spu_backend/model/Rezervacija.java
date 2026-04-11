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
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rezervacija")
@Getter
@Setter
@NoArgsConstructor
public class Rezervacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rezervacija_id")
    private Long rezervacijaId;

    @NotNull
    @Future
    @Column(nullable = false)
    private LocalDateTime datumVrijemeIsteka;

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

    public boolean istekla() {
        return LocalDateTime.now().isAfter(datumVrijemeIsteka);
    }

}
