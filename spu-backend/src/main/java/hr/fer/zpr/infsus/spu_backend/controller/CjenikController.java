package hr.fer.zpr.infsus.spu_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.fer.zpr.infsus.spu_backend.model.dto.CjenikFormDto;
import hr.fer.zpr.infsus.spu_backend.service.CjenikService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cjenici")
@RequiredArgsConstructor
public class CjenikController {

	private final CjenikService cjenikService;

	@PostMapping
	public String save(@Valid @ModelAttribute CjenikFormDto dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Neispravni podaci za cjenik.");
			return "redirect:/dogadaji/" + dto.getDogadajId();
		}

		try {
			cjenikService.save(dto);
			redirectAttributes.addFlashAttribute("successMessage", "Sektor je uspješno dodan.");

		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/dogadaji/" + dto.getDogadajId();
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute CjenikFormDto dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Neispravni podaci za cjenik.");
			return "redirect:/dogadaji/" + dto.getDogadajId();
		}

		try {
			cjenikService.update(id, dto);
			redirectAttributes.addFlashAttribute("successMessage", "Cijena je uspješno ažurirana.");

		} catch (IllegalArgumentException e) {

			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/dogadaji/" + dto.getDogadajId();
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, @ModelAttribute CjenikFormDto dto,
			RedirectAttributes redirectAttributes) {

		try {
			cjenikService.deleteById(id);
			redirectAttributes.addFlashAttribute("successMessage", "Sektor je uklonjen s događaja.");

		} catch (IllegalArgumentException e) {

			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/dogadaji/" + dto.getDogadajId();
	}
}
