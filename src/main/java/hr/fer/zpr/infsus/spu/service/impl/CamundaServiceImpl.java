package hr.fer.zpr.infsus.spu.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hr.fer.zpr.infsus.spu.dto.CamundaEventSubscriptionDto;
import hr.fer.zpr.infsus.spu.dto.CamundaProcessDataDto;
import hr.fer.zpr.infsus.spu.dto.CamundaTaskDto;
import hr.fer.zpr.infsus.spu.dto.StartProcessCamundaDto;
import hr.fer.zpr.infsus.spu.dto.WaitingApprovalDto;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.repository.DogadajRepository;
import hr.fer.zpr.infsus.spu.repository.DvoranaRepository;
import hr.fer.zpr.infsus.spu.service.CamundaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CamundaServiceImpl implements CamundaService {

	private static final String BASE = "http://localhost:8081/engine-rest";

	private final RestTemplate restTemplate = new RestTemplate();

	private final DvoranaRepository dvoranaRepository;

	private final DogadajRepository dogadajRepository;

	@Override
	public void startProcess(StartProcessCamundaDto dto) {

		Map<String, Object> body = Map.of("variables",
				Map.of("eventName", Map.of("value", dto.getEventName()), "dvoranaId",
						Map.of("value", dto.getDvoranaId()), "eventDate", Map.of("value", dto.getEventDate()),
						"organizator", Map.of("value", dto.getOrganizator())));

		restTemplate.postForObject(BASE + "/process-definition/key/eventApprovalProcess/start", body, String.class);
	}

	@Override
	public List<CamundaTaskDto> getTasks() {

		CamundaTaskDto[] tasks = restTemplate.getForObject(BASE + "/task", CamundaTaskDto[].class);

		return Arrays.asList(tasks);
	}

	@Override
	public void completeTask(String taskId, Map<String, Object> variables) {

		Map<String, Object> camundaVariables = new HashMap<>();

		variables.forEach((key, value) -> {

			camundaVariables.put(key, Map.of("value", value));
		});

		Map<String, Object> body = Map.of("variables", camundaVariables);

		restTemplate.postForObject(BASE + "/task/" + taskId + "/complete", body, String.class);
	}

	@Override
	public void approve(boolean approved, String processInstanceId) {

		Map<String, Object> body = Map.of(

				"messageName", "ADMIN_DECISION",

				"processInstanceId", processInstanceId,

				"processVariables", Map.of("approved", Map.of("value", approved)));

		restTemplate.postForObject(BASE + "/message", body, String.class);
	}

	@Override
	public List<CamundaTaskDto> getTasks(String assignee) {

		CamundaTaskDto[] tasks = restTemplate.getForObject(BASE + "/task?assignee=" + assignee, CamundaTaskDto[].class);

		List<CamundaTaskDto> result = Arrays.asList(tasks);

		result.forEach(task -> task.setProcessData(getVariables(task.getProcessInstanceId())));

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public CamundaProcessDataDto getVariables(String processInstanceId) {

		Map<String, Object> response = restTemplate
				.getForObject(BASE + "/process-instance/" + processInstanceId + "/variables", Map.class);

		CamundaProcessDataDto dto = new CamundaProcessDataDto();

		if (response.containsKey("eventName")) {
			dto.setEventName(((Map<String, Object>) response.get("eventName")).get("value").toString());
		}

		if (response.containsKey("dvoranaId")) {

			dto.setDvoranaId(Long.valueOf(((Map<String, Object>) response.get("dvoranaId")).get("value").toString()));
		}

		if (response.containsKey("eventDate")) {
			dto.setEventDate(
					LocalDateTime.parse(((Map<String, Object>) response.get("eventDate")).get("value").toString()));
		}

		if (dto.getDvoranaId() != null) {

			dvoranaRepository.findById(dto.getDvoranaId()).ifPresent(d -> dto.setDvoranaNaziv(d.getNaziv()));
		}

		return dto;
	}

	@Override
	public List<WaitingApprovalDto> getWaitingApprovals() {

		CamundaEventSubscriptionDto[] subscriptions = restTemplate
				.getForObject(BASE + "/event-subscription?eventType=message", CamundaEventSubscriptionDto[].class);

		return Arrays.stream(subscriptions)

				.filter(s -> "ADMIN_DECISION".equals(s.getEventName()))

				.map(s -> {

					WaitingApprovalDto dto = new WaitingApprovalDto();

					dto.setProcessInstanceId(s.getProcessInstanceId());

					dto.setProcessData(getVariables(s.getProcessInstanceId()));

					return dto;
				})

				.toList();
	}

	@Override
	public long countWaitingAdminApprovals() {

		CamundaEventSubscriptionDto[] subscriptions = restTemplate
				.getForObject(BASE + "/event-subscription?eventType=message", CamundaEventSubscriptionDto[].class);

		return Arrays.stream(subscriptions).filter(s -> "ADMIN_DECISION".equals(s.getEventName())).count();
	}

	public long countTasks(String assignee) {

		return getTasks(assignee).size();
	}

	@Override
	public void createEventFromProcess(String processInstanceId) {

		CamundaProcessDataDto dto = getVariables(processInstanceId);

		if (dogadajRepository.existsByDvorana_DvoranaIdAndDatumVrijemeOdrzavanja(dto.getDvoranaId(),
				dto.getEventDate())) {

			throw new IllegalArgumentException("Događaj već postoji u tom terminu.");
		}

		Dvorana dvorana = dvoranaRepository.findById(dto.getDvoranaId()).orElseThrow();
		Dogadaj dogadaj = new Dogadaj();
		dogadaj.setNaziv(dto.getEventName());
		dogadaj.setKategorija("Camunda");
		dogadaj.setOpis("Automatski kreiran kroz proces odobravanja događaja.");
		dogadaj.setDatumVrijemeOdrzavanja(dto.getEventDate());
		dogadaj.setDvorana(dvorana);

		dogadajRepository.save(dogadaj);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getUserGroup(String username) {

		List<Map<String, Object>> groups = restTemplate.getForObject(BASE + "/group?member=" + username, List.class);

		if (groups == null || groups.isEmpty()) {
			throw new RuntimeException("User nema grupu");
		}

		return groups.get(0).get("id").toString();
	}

	@Override
	public List<CamundaTaskDto> getTasksForUser(String username) {

		String group = getUserGroup(username);

		CamundaTaskDto[] tasks;

		if ("organizatori".equals(group)) {

			tasks = restTemplate.getForObject(BASE + "/task?assignee=" + username, CamundaTaskDto[].class);

		} else {

			tasks = restTemplate.getForObject(BASE + "/task?candidateUser=" + username, CamundaTaskDto[].class);
		}

		List<CamundaTaskDto> result = Arrays.asList(tasks);

		result.forEach(t -> t.setProcessData(getVariables(t.getProcessInstanceId())));

		return result;
	}
}