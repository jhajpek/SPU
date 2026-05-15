package hr.fer.zpr.infsus.spu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.fer.zpr.infsus.spu.dto.SektorFormDto;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import hr.fer.zpr.infsus.spu.service.SektorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/sektori")
@RequiredArgsConstructor
public class SektorController {

	private final SektorService sektorService;
	private final DvoranaService dvoranaService;

	@GetMapping
	public String findAll(@RequestParam(required = false) String naziv, @RequestParam(required = false) Long dvoranaId,
			Model model) {

		model.addAttribute("sektori", sektorService.search(naziv, dvoranaId));
		model.addAttribute("dvorane", dvoranaService.findAll());
		model.addAttribute("naziv", naziv);
		model.addAttribute("dvoranaId", dvoranaId);
		return "sectors/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {

		model.addAttribute("sektor", new SektorFormDto());
		model.addAttribute("dvorane", dvoranaService.findAll());
		return "sectors/form";
	}

	@PostMapping
	public String save(@Valid @ModelAttribute("sektor") SektorFormDto dto, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("dvorane", dvoranaService.findAll());
			return "sectors/form";
		}
		sektorService.save(dto);
		redirectAttributes.addFlashAttribute("successMessage", "Sektor je uspješno spremljen.");
		return "redirect:/sektori";
	}

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {

		model.addAttribute("sektor", sektorService.getFormDtoById(id));
		model.addAttribute("dvorane", dvoranaService.findAll());
		return "sectors/form";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("sektor") SektorFormDto dto,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("dvorane", dvoranaService.findAll());
			return "sectors/form";
		}

		try {
			sektorService.update(id, dto);
			redirectAttributes.addFlashAttribute("successMessage", "Sektor je uspješno ažuriran.");
			return "redirect:/sektori";

		} catch (IllegalArgumentException e) {
			model.addAttribute("dvorane", dvoranaService.findAll());
			model.addAttribute("errorMessage", e.getMessage());
			return "sectors/form";
		}

	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		sektorService.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Sektor je uspješno obrisan.");
		return "redirect:/sektori";
	}

}
