package hr.fer.zpr.infsus.spu.integration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;

import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DogadajIntegrationTests {

	@LocalServerPort
	private int port;

	private WebTestClient webTestClient;

	@Autowired
	private DogadajRepository dogadajRepository;

	@Autowired
	private DvoranaRepository dvoranaRepository;

	@Autowired
	private LokacijaRepository lokacijaRepository;

	@BeforeEach
	public void setUpWebTestClient() {

		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();

		dogadajRepository.deleteAll();
		dvoranaRepository.deleteAll();
		lokacijaRepository.deleteAll();

		Lokacija lokacija = EntityFactory.createLokacija();
		lokacija = lokacijaRepository.save(lokacija);

		Dvorana dvorana = EntityFactory.createDvorana("Dvorana 1", lokacija);
		dvorana = dvoranaRepository.save(dvorana);

		Dogadaj dogadaj = EntityFactory.createDogadaj("Koncert", dvorana);
		dogadajRepository.save(dogadaj);
	}

	@Test
	public void DogadajTestClient_GetDogadaji_ReturnListView() {

		webTestClient.get().uri("/dogadaji").exchange().expectStatus().isOk().expectBody(String.class).value(html -> {
			Assertions.assertThat(html).contains("Koncert");
		});
	}

	@Test
	public void DogadajTestClient_SaveDogadaj_ReturnRedirect() {

		Long dvoranaId = dvoranaRepository.findAll().get(0).getDvoranaId();

		webTestClient.post().uri("/dogadaji").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("naziv", "Festival").with("kategorija", "Festival")
						.with("opis", "Opis").with("dvoranaId", dvoranaId.toString()).with("datumVrijemeOdrzavanja",
								LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MINUTES).toString()))
				.exchange().expectStatus().is3xxRedirection();

		Assertions.assertThat(dogadajRepository.findAll()).anyMatch(d -> d.getNaziv().equals("Festival"));
	}

	@Test
	public void DogadajTestClient_SaveInvalidDogadaj_ReturnFormView() {

		Long dvoranaId = dvoranaRepository.findAll().get(0).getDvoranaId();

		webTestClient.post().uri("/dogadaji").contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters
				.fromFormData("naziv", "").with("kategorija", "Festival").with("dvoranaId", dvoranaId.toString()))
				.exchange().expectStatus().isOk().expectBody(String.class).value(html -> {
					Assertions.assertThat(html).contains("Spremi");
				});
	}

	@Test
	public void DogadajTestClient_GetEditForm_ReturnFormView() {

		Dogadaj dogadaj = dogadajRepository.findAll().get(0);

		webTestClient.get().uri("/dogadaji/edit/" + dogadaj.getDogadajId()).exchange().expectStatus().isOk()
				.expectBody(String.class).value(html -> {
					Assertions.assertThat(html).contains("Uređivanje događaja");

					Assertions.assertThat(html).contains(dogadaj.getNaziv());
				});
	}

	@Test
	public void DogadajTestClient_UpdateDogadaj_ReturnRedirect() {

		Dogadaj dogadaj = dogadajRepository.findAll().get(0);

		Long dvoranaId = dogadaj.getDvorana().getDvoranaId();

		webTestClient.post().uri("/dogadaji/edit/" + dogadaj.getDogadajId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("naziv", "Novi koncert").with("kategorija", "Glazba")
						.with("opis", "Opis").with("dvoranaId", dvoranaId.toString()).with("datumVrijemeOdrzavanja",
								LocalDateTime.now().plusDays(7).truncatedTo(ChronoUnit.MINUTES).toString()))
				.exchange().expectStatus().is3xxRedirection();

		Dogadaj updated = dogadajRepository.findById(dogadaj.getDogadajId()).orElseThrow();

		Assertions.assertThat(updated.getNaziv()).isEqualTo("Novi koncert");
		Assertions.assertThat(updated.getKategorija()).isEqualTo("Glazba");
	}

	@Test
	public void DogadajTestClient_DeleteDogadaj_ReturnRedirect() {

		Long id = dogadajRepository.findAll().get(0).getDogadajId();

		webTestClient.post().uri("/dogadaji/delete/" + id).exchange().expectStatus().is3xxRedirection();

		Assertions.assertThat(dogadajRepository.findById(id)).isEmpty();
	}

	@Test
	public void DogadajTestClient_GetDetails_ReturnDetailView() {

		Dogadaj dogadaj = dogadajRepository.findAll().stream().findFirst().orElseThrow();

		webTestClient.get().uri("/dogadaji/" + dogadaj.getDogadajId()).exchange().expectStatus().isOk()
				.expectBody(String.class).value(html -> {

					Assertions.assertThat(html).contains("Koncert");

					Assertions.assertThat(html).contains("Glazba");

					Assertions.assertThat(html).contains("Dvorana 1");
				});
	}

	@Test
	public void DogadajTestClient_UpdateInvalidDogadaj_ReturnFormView() {

		Dogadaj dogadaj = dogadajRepository.findAll().stream().findFirst().orElseThrow();

		Long dvoranaId = dogadaj.getDvorana().getDvoranaId();

		webTestClient.post().uri("/dogadaji/edit/" + dogadaj.getDogadajId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("naziv", "").with("kategorija", "Glazba").with("opis", "Opis")
						.with("dvoranaId", dvoranaId.toString()))
				.exchange().expectStatus().isOk().expectBody(String.class).value(html -> {

					Assertions.assertThat(html).contains("Spremi");
				});
	}

	@Test
	public void DogadajTestClient_SearchDogadaji_ReturnFilteredResults() {

		webTestClient.get().uri("/dogadaji?naziv=Koncert").exchange().expectStatus().isOk().expectBody(String.class)
				.value(html -> {

					Assertions.assertThat(html).contains("Koncert");
				});
	}

	@Test
	public void DogadajTestClient_GetInvalidDogadaj_ReturnServerError() {

		webTestClient.get().uri("/dogadaji/999999").exchange().expectStatus().is3xxRedirection();
	}
}
