package hr.fer.zpr.infsus.spu.model;

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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "cjenik",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_dogadaj_sektor",
                        columnNames = {"dogadaj_id", "sektor_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Cjenik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cjenik_id")
    private Long cjenikId;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal cijena;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dogadaj_id", nullable = false)
    private Dogadaj dogadaj;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sektor_id", nullable = false)
    private Sektor sektor;

}
