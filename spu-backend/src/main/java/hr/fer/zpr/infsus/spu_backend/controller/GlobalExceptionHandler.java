package hr.fer.zpr.infsus.spu_backend.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgument(IllegalArgumentException ex, Model model) {

		model.addAttribute("errorMessage", ex.getMessage());
		return "error/error";
	}

	@ExceptionHandler(Exception.class)
	public String handleGeneralException(Exception ex, Model model) {

		model.addAttribute("errorMessage", "Dogodila se pogreška.");
		return "error/error";
	}
}
