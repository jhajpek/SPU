package hr.fer.zpr.infsus.spu.integration;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.repository.CjenikRepository;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.repository.SektorRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CjenikIntegrationTests {

	@LocalServerPort
	private int port;

	private WebTestClient webTestClient;

	@Autowired
	private CjenikRepository cjenikRepository;

	@Autowired
	private DogadajRepository dogadajRepository;

	@Autowired
	private DvoranaRepository dvoranaRepository;

	@Autowired
	private LokacijaRepository lokacijaRepository;

	@Autowired
	private SektorRepository sektorRepository;

	@BeforeEach
	public void setUpWebTestClient() {

		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();

		cjenikRepository.deleteAll();
		sektorRepository.deleteAll();
		dogadajRepository.deleteAll();
		dvoranaRepository.deleteAll();
		lokacijaRepository.deleteAll();

		Lokacija lokacija = EntityFactory.createLokacija();
		lokacija = lokacijaRepository.save(lokacija);

		Dvorana dvorana = EntityFactory.createDvorana("Arena Zagreb", lokacija);
		dvorana = dvoranaRepository.save(dvorana);

		Sektor sektor = EntityFactory.createSektor("VIP", 100, dvorana);
		sektor = sektorRepository.save(sektor);

		Dogadaj dogadaj = EntityFactory.createDogadaj("Koncert", dvorana);
		dogadaj = dogadajRepository.save(dogadaj);

		Cjenik cjenik = EntityFactory.createCjenik(BigDecimal.valueOf(20), dogadaj, sektor);
		cjenikRepository.save(cjenik);
	}

	@Test
	public void CjenikTestClient_SaveCjenik_ReturnRedirect() {

		Dogadaj dogadaj = dogadajRepository.findAll().stream().findFirst().orElseThrow();

		Sektor noviSektor = EntityFactory.createSektor("Parter", 150, dogadaj.getDvorana());
		noviSektor = sektorRepository.save(noviSektor);

		webTestClient.post().uri("/cjenici").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("dogadajId", dogadaj.getDogadajId().toString())
						.with("sektorId", noviSektor.getSektorId().toString()).with("cijena", "35"))
				.exchange().expectStatus().is3xxRedirection();

		Assertions.assertThat(cjenikRepository.findAll()).hasSize(2);
	}

	@Test
	public void CjenikTestClient_SaveInvalidCjenik_ReturnRedirect() {

		Dogadaj dogadaj = dogadajRepository.findAll().stream().findFirst().orElseThrow();

		webTestClient.post().uri("/cjenici").contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters
				.fromFormData("dogadajId", dogadaj.getDogadajId().toString()).with("sektorId", "").with("cijena", ""))
				.exchange().expectStatus().is3xxRedirection();
	}

	@Test
	public void CjenikTestClient_UpdateCjenik_ReturnRedirect() {

		Cjenik cjenik = cjenikRepository.findAll().stream().findFirst().orElseThrow();

		webTestClient.post().uri("/cjenici/edit/" + cjenik.getCjenikId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("dogadajId", cjenik.getDogadaj().getDogadajId().toString())
						.with("sektorId", cjenik.getSektor().getSektorId().toString()).with("cijena", "45"))
				.exchange().expectStatus().is3xxRedirection();

		Cjenik updated = cjenikRepository.findById(cjenik.getCjenikId()).orElseThrow();

		Assertions.assertThat(updated.getCijena()).isEqualByComparingTo("45");
	}

	@Test
	public void CjenikTestClient_DeleteCjenik_ReturnRedirect() {

		Cjenik cjenik = cjenikRepository.findAll().stream().findFirst().orElseThrow();

		webTestClient.post().uri("/cjenici/delete/" + cjenik.getCjenikId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("dogadajId", cjenik.getDogadaj().getDogadajId().toString())).exchange()
				.expectStatus().is3xxRedirection();

		Assertions.assertThat(cjenikRepository.findById(cjenik.getCjenikId())).isEmpty();
	}

	@Test
	public void CjenikTestClient_SaveDuplicateCjenik_ReturnRedirect() {

		Dogadaj dogadaj = dogadajRepository.findAll().stream().findFirst().orElseThrow();

		Sektor sektor = sektorRepository.findAll().stream().findFirst().orElseThrow();

		webTestClient.post().uri("/cjenici").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("dogadajId", dogadaj.getDogadajId().toString())
						.with("sektorId", sektor.getSektorId().toString()).with("cijena", "50"))
				.exchange().expectStatus().is3xxRedirection();

		Assertions.assertThat(cjenikRepository.findAll()).hasSize(1);
	}
}