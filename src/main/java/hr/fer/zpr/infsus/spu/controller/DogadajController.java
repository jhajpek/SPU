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

import hr.fer.zpr.infsus.spu.dto.DogadajDetailsDto;
import hr.fer.zpr.infsus.spu.dto.DogadajFormDto;
import hr.fer.zpr.infsus.spu.service.DogadajService;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dogadaji")
@RequiredArgsConstructor
public class DogadajController {

	private final DogadajService dogadajService;
	private final DvoranaService dvoranaService;

	@ModelAttribute("dvorane")
	public Object dvorane() {
		return dvoranaService.findAll();
	}

	@GetMapping
	public String findAll(@RequestParam(required = false) String naziv,
			@RequestParam(required = false) String kategorija, Model model) {

		model.addAttribute("dogadaji", dogadajService.search(naziv, kategorija));
		model.addAttribute("naziv", naziv);
		model.addAttribute("kategorija", kategorija);
		return "events/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("dogadaj", new DogadajFormDto());
		return "events/form";
	}

	@PostMapping
	public String save(@Valid @ModelAttribute DogadajFormDto dto, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "events/form";
		}

		dogadajService.save(dto);
		redirectAttributes.addFlashAttribute("successMessage", "Događaj je uspješno spremljen.");
		return "redirect:/dogadaji";
	}

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {

		model.addAttribute("dogadaj", dogadajService.getFormDtoById(id));
		return "events/form";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("dogadaj") DogadajFormDto dto,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			dto.setDogadajId(id);
			model.addAttribute("dogadaj", dto);
			return "events/form";
		}

		try {
			dogadajService.update(id, dto);
			redirectAttributes.addFlashAttribute("successMessage", "Događaj je uspješno ažuriran.");
			return "redirect:/dogadaji";

		} catch (IllegalArgumentException e) {
			dto.setDogadajId(id);
			model.addAttribute("dogadaj", dto);
			model.addAttribute("errorMessage", e.getMessage());
			return "events/form";
		}
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		dogadajService.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Događaj je uspješno izbrisan.");
		return "redirect:/dogadaji";
	}

	@GetMapping("/{id}")
	public String details(@PathVariable Long id, Model model) {
		DogadajDetailsDto dto = dogadajService.getDetailsById(id);
		model.addAttribute("details", dto);
		model.addAttribute("noviCjenik", dto.getNoviCjenik());
		return "events/detail";
	}

}
