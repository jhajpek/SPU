package hr.fer.zpr.infsus.spu.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import hr.fer.zpr.infsus.spu.dto.StartProcessCamundaDto;
import hr.fer.zpr.infsus.spu.service.CamundaService;
import hr.fer.zpr.infsus.spu.service.DogadajService;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/camunda-dashboard")
@RequiredArgsConstructor
public class CamundaDemoController {

	private final CamundaService camundaService;

	private final DvoranaService dvoranaService;

	private final DogadajService dogadajService;

	@GetMapping
	public String index(Model model) {

		model.addAttribute("marioCount", camundaService.getTasksForUser("mario").size());
		model.addAttribute("jozoCount", camundaService.getTasksForUser("jozo").size());
		model.addAttribute("ronaldoCount", camundaService.countWaitingAdminApprovals());

		return "camunda/dashboard";
	}

	@PostMapping("/start")
	public String start(@ModelAttribute StartProcessCamundaDto dto) {

		dto.setOrganizator("mario");

		camundaService.startProcess(dto);

		return "redirect:/camunda-dashboard";
	}

	@PostMapping("/valid/{taskId}")
	public String valid(@PathVariable String taskId) {

		camundaService.completeTask(taskId, Map.of("valid", true));

		return "redirect:/camunda-dashboard/jozo";
	}

	@PostMapping("/invalid/{taskId}")
	public String invalid(@PathVariable String taskId) {

		camundaService.completeTask(taskId, Map.of("valid", false));

		return "redirect:/camunda-dashboard/jozo";
	}

	@PostMapping("/approve")
	public String approve(@RequestParam String processInstanceId) {

		camundaService.createEventFromProcess(processInstanceId);

		camundaService.approve(true, processInstanceId);

		return "redirect:/camunda-dashboard/ronaldo";
	}

	@PostMapping("/reject")
	public String reject(@RequestParam String processInstanceId) {

		camundaService.approve(false, processInstanceId);

		return "redirect:/camunda-dashboard/ronaldo";
	}

	@PostMapping("/changeDate/{taskId}")
	public String changeDate(@PathVariable String taskId, @RequestParam String eventDate) {

		camundaService.completeTask(taskId, Map.of("eventDate", eventDate));

		return "redirect:/camunda-dashboard/mario";
	}

	@PostMapping("/fixEvent/{taskId}")
	public String fixEvent(@PathVariable String taskId, @RequestParam String eventName, @RequestParam Long dvoranaId,
			@RequestParam String eventDate) {

		camundaService.completeTask(taskId,
				Map.of("eventName", eventName, "dvoranaId", dvoranaId, "eventDate", eventDate, "valid", true));

		return "redirect:/camunda-dashboard/mario";
	}

	@GetMapping("/{username}")
	public String portal(@PathVariable String username, Model model) {

		model.addAttribute("startProcess", new StartProcessCamundaDto());
		model.addAttribute("username", username);
		model.addAttribute("tasks", camundaService.getTasksForUser(username));
		model.addAttribute("dvorane", dvoranaService.findAll());
		model.addAttribute("dogadaji", dogadajService.findAll());

		String group = camundaService.getUserGroup(username);

		model.addAttribute("group", group);

		if ("admin".equals(group)) {

			model.addAttribute("waitingApprovals", camundaService.getWaitingApprovals());

		} else {

			model.addAttribute("waitingApprovals", java.util.Collections.emptyList());
		}

		return "camunda/portal";
	}

}