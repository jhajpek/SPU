package hr.fer.zpr.infsus.spu.integration;

import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.repository.LokacijaRepository;
import hr.fer.zpr.infsus.spu.util.EntityFactory;
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

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DvoranaIntegrationTests {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @Autowired
    private LokacijaRepository lokacijaRepository;

    @Autowired
    private DvoranaRepository dvoranaRepository;

    @BeforeEach
    public void setUpWebTestClient() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();

        dvoranaRepository.deleteAll();
        lokacijaRepository.deleteAll();

        Lokacija lokacija1 = EntityFactory.createLokacija();
        Lokacija lokacija2 = EntityFactory.createLokacija();
        lokacija2.setMjesto("Rijeka");
        Lokacija lokacija3 = EntityFactory.createLokacija();
        lokacija3.setMjesto("Split");
        lokacijaRepository.saveAll(List.of(lokacija1, lokacija2, lokacija3));

        Dvorana dvorana1 = EntityFactory.createDvorana("Dvorana 1", lokacija1);
        Dvorana dvorana2 = EntityFactory.createDvorana("Dvorana 2", lokacija2);
        dvoranaRepository.saveAll(List.of(dvorana1, dvorana2));
    }

    @Test
    public void DvoranaTestClient_GetDvorane_ReturnListView() {
        webTestClient.get()
                .uri("/dvorane")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    Assertions.assertThatCharSequence(html).contains("Popis dvorana");
                    Assertions.assertThatCharSequence(html).contains("Dodaj dvoranu");
                });

        Assertions.assertThat(dvoranaRepository.count()).isEqualTo(2L);
    }

    @Test
    public void DvoranaTestClient_GetDvoraneWithSearchQuery_ReturnListView() {
        webTestClient.get()
                .uri("/dvorane?query=ijek")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    Assertions.assertThatCharSequence(html).contains("Popis dvorana");
                    Assertions.assertThatCharSequence(html).contains("Dodaj dvoranu");
                    Assertions.assertThatCharSequence(html).contains("Rijeka");
                });
    }

    @Test
    public void DvoranaTestClient_GetDvoranaForm_ReturnFormView() {
        webTestClient.get()
                .uri("/dvorane/new")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    Assertions.assertThat(html).contains("Spremi");
                });
    }

    @Test
    public void DvoranaTestClient_SaveDvorana_ReturnListView() {
        Long lokacijaId = lokacijaRepository.findAll().get(2).getLokacijaId();

        webTestClient.post()
                .uri("/dvorane")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("naziv", "Dvorana 3")
                        .with("lokacijaId", lokacijaId.toString()))
                .exchange()
                .expectStatus().is3xxRedirection();

        Assertions.assertThat(dvoranaRepository.findAll()).anyMatch(d -> d.getNaziv().equals("Dvorana 3"));
    }

    @Test
    public void DvoranaTestClient_SaveInvalidDvorana_ReturnListView() {
        webTestClient.post()
                .uri("/dvorane")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("naziv", "")
                        .with("lokacijaId", "1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    Assertions.assertThat(html).contains("Spremi");
                });
    }

    @Test
    public void DvoranaTestClient_GetEditForm_ReturnFormView() {
        Dvorana dvorana = dvoranaRepository.findAll().get(0);

        webTestClient.get()
                .uri("/dvorane/edit/" + dvorana.getDvoranaId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    Assertions.assertThat(html).contains("Naziv dvorane");
                    Assertions.assertThat(html).contains(dvorana.getNaziv());
                });
    }

    @Test
    public void DvoranaTestClient_UpdateDvorana_ReturnListView() {
        Dvorana dvorana = dvoranaRepository.findAll().get(0);
        Long lokacijaId = dvorana.getLokacija().getLokacijaId();

        webTestClient.post()
                .uri("/dvorane/edit/" + dvorana.getDvoranaId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("naziv", "Dvorana 0")
                        .with("lokacijaId", lokacijaId.toString()))
                .exchange()
                .expectStatus().is3xxRedirection();

        Dvorana updatedDvorana = dvoranaRepository.findById(dvorana.getDvoranaId()).orElseThrow();
        Assertions.assertThat(updatedDvorana.getNaziv()).isEqualTo("Dvorana 0");
    }

    @Test
    public void DvoranaTestClient_UpdateInvalidDvorana_ReturnFormView() {
        Dvorana dvorana = dvoranaRepository.findAll().get(0);
        Long lokacijaId = dvorana.getLokacija().getLokacijaId();
        String dvoranaNaziv = dvorana.getNaziv();

        webTestClient.post()
                .uri("/dvorane/edit/" + dvorana.getDvoranaId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("naziv", "")
                        .with("lokacijaId", lokacijaId.toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(html -> {
                    Assertions.assertThat(html).contains("Spremi");
                });

        Dvorana notUpdatedDvorana = dvoranaRepository.findById(dvorana.getDvoranaId()).orElseThrow();
        Assertions.assertThat(notUpdatedDvorana.getNaziv()).isEqualTo(dvoranaNaziv);
    }

    @Test
    public void DvoranaTestClient_DeleteDvorana_ReturnListView() {
        Long dvoranaId = dvoranaRepository.findAll().get(0).getDvoranaId();

        webTestClient.post()
                .uri("/dvorane/delete/" + dvoranaId)
                .exchange()
                .expectStatus().is3xxRedirection();

        Assertions.assertThat(dvoranaRepository.findById(dvoranaId)).isEqualTo(Optional.empty());
    }

}
