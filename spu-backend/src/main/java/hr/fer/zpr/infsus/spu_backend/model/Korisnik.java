package hr.fer.zpr.infsus.spu_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "korisnik")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "korisnik_id")
    private Long korisnikId;

    @Email
    @NotBlank
    @Size(max = 50)
    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 500)
    @Column(length = 500, nullable = false)
    private String lozinka;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String ime;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String prezime;

}
