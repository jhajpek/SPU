package hr.fer.zpr.infsus.spu_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "kupac")
@Getter
@Setter
@NoArgsConstructor
public class Kupac extends Korisnik {

}
