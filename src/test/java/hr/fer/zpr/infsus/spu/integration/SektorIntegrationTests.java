package hr.fer.zpr.infsus.spu.integration;

import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.repository.SektorRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SektorIntegrationTests {

	@LocalServerPort
	private int port;

	private WebTestClient webTestClient;

	@Autowired
	private SektorRepository sektorRepository;

	@Autowired
	private DvoranaRepository dvoranaRepository;

	@Autowired
	private LokacijaRepository lokacijaRepository;

	@BeforeEach
	public void setUpWebTestClientAndRepositories() {

		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();

		Lokacija lokacija = EntityFactory.createLokacija();
		lokacija = lokacijaRepository.save(lokacija);

		Dvorana dvorana = EntityFactory.createDvorana("Arena Zagreb", lokacija);
		dvorana = dvoranaRepository.save(dvorana);

		Sektor sektor = EntityFactory.createSektor("Parter", 100, dvorana);

		sektorRepository.save(sektor);
	}

	@AfterEach
	public void cleanUpRepositories() {
		sektorRepository.deleteAll();
		dvoranaRepository.deleteAll();
		lokacijaRepository.deleteAll();
	}

	@Test
	public void SektorTestClient_GetSektori_ReturnListView() {

		webTestClient.get().uri("/sektori").exchange().expectStatus().isOk().expectBody(String.class).value(html -> {

			Assertions.assertThat(html).contains("Parter");
		});
	}

	@Test
	public void SektorTestClient_SaveSektor_ReturnRedirect() {

		Long dvoranaId = dvoranaRepository.findAll().stream().findFirst().orElseThrow().getDvoranaId();

		webTestClient
				.post().uri("/sektori").contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters
						.fromFormData("naziv", "VIP").with("kapacitet", "50").with("dvoranaId", dvoranaId.toString()))
				.exchange().expectStatus().is3xxRedirection();

		Assertions.assertThat(sektorRepository.findAll()).hasSize(2).anyMatch(s -> s.getNaziv().equals("VIP"));
	}

	@Test
	public void SektorTestClient_SaveInvalidSektor_ReturnFormView() {

		Long dvoranaId = dvoranaRepository.findAll().stream().findFirst().orElseThrow().getDvoranaId();

		webTestClient
				.post().uri("/sektori").contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters
						.fromFormData("naziv", "").with("kapacitet", "50").with("dvoranaId", dvoranaId.toString()))
				.exchange().expectStatus().isOk().expectBody(String.class).value(html -> {

					Assertions.assertThat(html).contains("Spremi");
				});
	}

	@Test
	public void SektorTestClient_GetEditForm_ReturnFormView() {

		Sektor sektor = sektorRepository.findAll().stream().findFirst().orElseThrow();

		webTestClient.get().uri("/sektori/edit/" + sektor.getSektorId()).exchange().expectStatus().isOk()
				.expectBody(String.class).value(html -> {

					Assertions.assertThat(html).contains("Uređivanje sektora");

					Assertions.assertThat(html).contains(sektor.getNaziv());
				});
	}

	@Test
	public void SektorTestClient_UpdateSektor_ReturnRedirect() {

		Sektor sektor = sektorRepository.findAll().stream().findFirst().orElseThrow();

		Long dvoranaId = sektor.getDvorana().getDvoranaId();

		webTestClient.post().uri("/sektori/edit/" + sektor.getSektorId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromFormData("naziv", "VIP GOLD")
						.with("kapacitet", "120").with("dvoranaId", dvoranaId.toString()))
				.exchange().expectStatus().is3xxRedirection();

		Sektor updated = sektorRepository.findById(sektor.getSektorId()).orElseThrow();

		Assertions.assertThat(updated.getNaziv()).isEqualTo("VIP GOLD");

		Assertions.assertThat(updated.getKapacitet()).isEqualTo(120);
	}

	@Test
	public void SektorTestClient_DeleteSektor_ReturnRedirect() {

		Long sektorId = sektorRepository.findAll().stream().findFirst().orElseThrow().getSektorId();

		webTestClient.post().uri("/sektori/delete/" + sektorId).exchange().expectStatus().is3xxRedirection();

		Assertions.assertThat(sektorRepository.findById(sektorId)).isEmpty();
	}
}