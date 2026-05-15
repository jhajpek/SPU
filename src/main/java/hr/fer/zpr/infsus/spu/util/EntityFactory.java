package hr.fer.zpr.infsus.spu.util;

import hr.fer.zpr.infsus.spu.model.Administrator;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Korisnik;
import hr.fer.zpr.infsus.spu.model.Kupac;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Sektor;

import java.time.LocalDateTime;

public class EntityFactory {

    public static Korisnik createKorisnik() {
        Korisnik korisnik = new Korisnik();
        korisnik.setIme("Marko");
        korisnik.setPrezime("Mamić");
        korisnik.setEmail("marko.mamic@gmail.com");
        korisnik.setLozinka("$2a$12$0EtFHHKP7awf6Jd0zZraMu8ZRhXpan63DVisMvvr/WmnHoCm9T1Dm");
        return korisnik;
    }

    public static Kupac createKupac() {
        Kupac kupac = new Kupac();
        kupac.setIme("Marko");
        kupac.setPrezime("Mamić");
        kupac.setEmail("marko.mamic@gmail.com");
        kupac.setLozinka("$2a$12$0EtFHHKP7awf6Jd0zZraMu8ZRhXpan63DVisMvvr/WmnHoCm9T1Dm");
        return kupac;
    }

    public static Administrator createAdministrator() {
        Administrator administrator = new Administrator();
        administrator.setIme("Marko");
        administrator.setPrezime("Mamić");
        administrator.setEmail("marko.mamic@gmail.com");
        administrator.setLozinka("$2a$12$0EtFHHKP7awf6Jd0zZraMu8ZRhXpan63DVisMvvr/WmnHoCm9T1Dm");
        return administrator;
    }

    public static Lokacija createLokacija() {
        Lokacija lokacija = new Lokacija();
        lokacija.setUlica("Jarunska ulica");
        lokacija.setKucniBroj(2);
        lokacija.setPostanskiBroj("10000");
        lokacija.setMjesto("Zagreb");
        return lokacija;
    }

    public static Dvorana createDvorana(String naziv, Lokacija lokacija) {
        Dvorana dvorana = new Dvorana();
        dvorana.setNaziv(naziv);
        dvorana.setLokacija(lokacija);
        return dvorana;
    }

    public static Sektor createSektor(String naziv, Integer kapacitet, Dvorana dvorana) {
        Sektor sektor = new Sektor();
        sektor.setNaziv(naziv);
        sektor.setKapacitet(kapacitet);
        sektor.setDvorana(dvorana);
        return sektor;
    }

    public static Dogadaj createDogadaj(String naziv, Dvorana dvorana) {
        Dogadaj dogadaj = new Dogadaj();
        dogadaj.setNaziv(naziv);
        dogadaj.setKategorija("Glazba");
        dogadaj.setOpis("");
        dogadaj.setDatumVrijemeOdrzavanja(LocalDateTime.now().plusDays(7));
        dogadaj.setDvorana(dvorana);
        return dogadaj;
    }

}
