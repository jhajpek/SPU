package hr.fer.zpr.infsus.spu.config;

import java.time.LocalDateTime;
import java.util.Map;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CamundaWorkerConfig {

	private final DogadajRepository dogadajRepository;

	@Bean
	public ExternalTaskClient externalTaskClient() {

		ExternalTaskClient client = ExternalTaskClient.create().baseUrl("http://localhost:8081/engine-rest").disableBackoffStrategy().build();

		client.subscribe("hall-check")

				.lockDuration(1000)

				.handler((task, service) -> {
					
					System.err.println("VRŠIM PROVJERU: provjeravam postoji li događaj u to vrijeme u toj dvorani...");

					Long dvoranaId = Long.valueOf(task.getVariable("dvoranaId").toString());

					String eventDate = task.getVariable("eventDate");

					boolean occupied = dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(dvoranaId,
							LocalDateTime.parse(eventDate));

					service.complete(task, Map.of("hallAvailable", !occupied));
				})

				.open();

		client.subscribe("admin-reminder")

				.lockDuration(1000)

				.handler((task, service) -> {

					System.err.println("MAIL FIKTIVNI: admin obaviješten da ima događaja...");

					service.complete(task);

				})

				.open();

		return client;
	}
}
