package hr.fer.zpr.infsus.spu.util;

import hr.fer.zpr.infsus.spu.model.Administrator;
import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Korisnik;
import hr.fer.zpr.infsus.spu.model.Kupac;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Rezervacija;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.model.Sjedalo;
import hr.fer.zpr.infsus.spu.model.Ulaznica;

import java.math.BigDecimal;
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

	public static Sjedalo createSjedalo(Integer red, Integer broj, Sektor sektor) {
		Sjedalo sjedalo = new Sjedalo();
		sjedalo.setRed(red);
		sjedalo.setBroj(broj);
		sjedalo.setSektor(sektor);
		return sjedalo;
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

	public static Cjenik createCjenik(BigDecimal cijena, Dogadaj dogadaj, Sektor sektor) {
		Cjenik cjenik = new Cjenik();
		cjenik.setCijena(cijena);
		cjenik.setDogadaj(dogadaj);
		cjenik.setSektor(sektor);
		return cjenik;
	}

	public static Rezervacija createRezervacija(Kupac kupac, Dogadaj dogadaj, Sjedalo sjedalo) {
		Rezervacija rezervacija = new Rezervacija();
		rezervacija.setDatumVrijemeIsteka(LocalDateTime.now().plusMinutes(15));
		rezervacija.setKupac(kupac);
		rezervacija.setDogadaj(dogadaj);
		rezervacija.setSjedalo(sjedalo);
		return rezervacija;
	}

	public static Ulaznica createUlaznica(Kupac kupac, Dogadaj dogadaj, Sjedalo sjedalo) {
		Ulaznica ulaznica = new Ulaznica();
		ulaznica.setQrKod(dogadaj.getDogadajId() + " " + sjedalo.getSjedaloId());
		ulaznica.setKupac(kupac);
		ulaznica.setDogadaj(dogadaj);
		ulaznica.setSjedalo(sjedalo);
		return ulaznica;
	}

}
