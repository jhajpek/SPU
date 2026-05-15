package hr.fer.zpr.infsus.spu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.fer.zpr.infsus.spu.dto.CjenikFormDto;
import hr.fer.zpr.infsus.spu.service.CjenikService;
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
			System.out.println(bindingResult.getAllErrors());
			redirectAttributes.addFlashAttribute("errorMessage", "Neispravni podaci za cjenik.");
			return "redirect:/dogadaji/" + dto.getDogadajId();
		}
		cjenikService.save(dto);
		redirectAttributes.addFlashAttribute("successMessage", "Sektor je uspješno dodan.");
		return "redirect:/dogadaji/" + dto.getDogadajId();
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute CjenikFormDto dto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Neispravni podaci za cjenik.");
			return "redirect:/dogadaji/" + dto.getDogadajId();
		}

		cjenikService.update(id, dto);
		redirectAttributes.addFlashAttribute("successMessage", "Cijena je uspješno ažurirana.");
		return "redirect:/dogadaji/" + dto.getDogadajId();
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, @ModelAttribute CjenikFormDto dto,
			RedirectAttributes redirectAttributes) {

		cjenikService.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Sektor je uklonjen s događaja.");
		return "redirect:/dogadaji/" + dto.getDogadajId();
	}

}
