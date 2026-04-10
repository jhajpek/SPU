package hr.fer.zpr.infsus.spu_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "administrator")
@Getter
@Setter
@NoArgsConstructor
public class Administrator extends Korisnik {

}
