package hr.fer.zpr.infsus.spu_backend.controller;

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

import hr.fer.zpr.infsus.spu_backend.model.Dogadaj;
import hr.fer.zpr.infsus.spu_backend.model.dto.DogadajFormDto;
import hr.fer.zpr.infsus.spu_backend.service.CjenikService;
import hr.fer.zpr.infsus.spu_backend.service.DogadajService;
import hr.fer.zpr.infsus.spu_backend.service.DvoranaService;
import hr.fer.zpr.infsus.spu_backend.service.SektorService;
import hr.fer.zpr.infsus.spu_backend.service.UlaznicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/dogadaji")
@RequiredArgsConstructor
public class DogadajController {

	private final DogadajService dogadajService;
	private final DvoranaService dvoranaService;
	private final CjenikService cjenikService;
	private final UlaznicaService ulaznicaService;
	private final SektorService sektorService;

	@GetMapping
	public String findAll(@RequestParam(required = false) String naziv,
			@RequestParam(required = false) String kategorija, Model model) {

		model.addAttribute("dogadaji", dogadajService.search(naziv, kategorija));
		model.addAttribute("naziv", naziv);
		model.addAttribute("kategorija", kategorija);

		return "dogadaji/list";
	}

	@GetMapping("/new")
	public String createForm(@RequestParam(required = false) Long dvoranaId, Model model) {

		DogadajFormDto dto = new DogadajFormDto();
		dto.setDvoranaId(dvoranaId);
		model.addAttribute("dogadaj", dto);
		model.addAttribute("dvorane", dvoranaService.findAll());
		if (dvoranaId != null) {
			model.addAttribute("sektori", sektorService.findByDvorana(dvoranaId));
		}

		return "dogadaji/form";
	}

	@PostMapping
	public String save(@Valid @ModelAttribute DogadajFormDto dto, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("dvorane", dvoranaService.findAll());
			if (dto.getDvoranaId() != null) {
				model.addAttribute("sektori", sektorService.findByDvorana(dto.getDvoranaId()));
			}

			return "dogadaji/form";
		}

		dogadajService.save(dto);
		redirectAttributes.addFlashAttribute("successMessage", "Događaj je uspješno spremljen.");

		return "redirect:/dogadaji";
	}

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {

		DogadajFormDto dto = dogadajService.getFormDtoById(id);
		model.addAttribute("dogadaj", dto);
		model.addAttribute("dvorane", dvoranaService.findAll());
		model.addAttribute("sektori", sektorService.findByDvorana(dto.getDvoranaId()));

		return "dogadaji/form";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("dogadaj") DogadajFormDto dto,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("dvorane", dvoranaService.findAll());
			model.addAttribute("sektori", sektorService.findByDvorana(dto.getDvoranaId()));
			return "dogadaji/form";
		}

		dogadajService.update(id, dto);
		redirectAttributes.addFlashAttribute("successMessage", "Događaj je uspješno ažuriran.");

		return "redirect:/dogadaji";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			dogadajService.deleteById(id);
			redirectAttributes.addFlashAttribute("successMessage", "Događaj je uspješno izbrisan.");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/dogadaji";
	}

	@GetMapping("/{id}")
	public String details(@PathVariable Long id, Model model) {

		Dogadaj dogadaj = dogadajService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Događaj ne postoji."));

		model.addAttribute("dogadaj", dogadaj);
		model.addAttribute("cjenici", cjenikService.findByDogadaj(id));
		model.addAttribute("prodaneUlaznice", ulaznicaService.getSoldTicketsBySektor(id));

		return "dogadaji/detail";
	}
}