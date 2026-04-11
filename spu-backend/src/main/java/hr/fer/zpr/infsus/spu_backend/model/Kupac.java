package hr.fer.zpr.infsus.spu_backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "kupac")
    private List<Ulaznica> ulaznice = new ArrayList<>();

    @OneToMany(mappedBy = "kupac")
    private List<Rezervacija> rezervacije = new ArrayList<>();

    public void kupiUlaznicu() {
    }

    public List<Ulaznica> pregledajKupnje() {
        return ulaznice;
    }

}
