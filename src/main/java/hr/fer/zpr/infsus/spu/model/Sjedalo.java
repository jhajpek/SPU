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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sjedalo")
@Getter
@Setter
@NoArgsConstructor
public class Sjedalo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sjedalo_id")
    private Long sjedaloId;

    @NotNull
    @Min(value = 1)
    @Column(nullable = false)
    private Integer red;

    @NotNull
    @Min(value = 1)
    @Column(nullable = false)
    private Integer broj;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sektor_id", nullable = false)
    private Sektor sektor;

}
