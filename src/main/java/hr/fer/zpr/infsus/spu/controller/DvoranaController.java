package hr.fer.zpr.infsus.spu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.fer.zpr.infsus.spu.dto.DvoranaFormDto;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import hr.fer.zpr.infsus.spu.service.LokacijaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dvorane")
@RequiredArgsConstructor
public class DvoranaController {

	private final DvoranaService dvoranaService;
	private final LokacijaService lokacijaService;

	@GetMapping
	public String findAll(@RequestParam(required = false) String query, Model model) {

		model.addAttribute("dvorane", dvoranaService.search(query));
		model.addAttribute("query", query);
		return "halls/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {

		model.addAttribute("dvorana", new DvoranaFormDto());
		model.addAttribute("lokacije", lokacijaService.findAll());
		return "halls/form";
	}

	@PostMapping
	public String save(@Valid @ModelAttribute("dvorana") DvoranaFormDto dto, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("lokacije", lokacijaService.findAll());
			return "halls/form";
		}

		dvoranaService.save(dto);
		redirectAttributes.addFlashAttribute("successMessage", "Dvorana je uspješno spremljena.");
		return "redirect:/dvorane";
	}

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {

		model.addAttribute("dvorana", dvoranaService.getFormDtoById(id));
		model.addAttribute("lokacije", lokacijaService.findAll());
		return "halls/form";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("dvorana") DvoranaFormDto dto,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("lokacije", lokacijaService.findAll());
			return "halls/form";
		}

		dvoranaService.update(id, dto);
		redirectAttributes.addFlashAttribute("successMessage", "Dvorana je uspješno spremljena.");
		return "redirect:/dvorane";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		dvoranaService.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Dvorana je uspješno obrisana.");
		return "redirect:/dvorane";
	}

}
