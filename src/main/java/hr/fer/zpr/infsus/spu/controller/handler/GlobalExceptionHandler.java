package hr.fer.zpr.infsus.spu.controller.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error/error";
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNoResourceFound(NoResourceFoundException ex, Model model) {
		model.addAttribute("errorMessage", "Stranica koju tražite ne postoji.");
		return "error/error";
	}

	@ExceptionHandler(Exception.class)
	public String handleGeneralException(Exception ex, Model model) {
		model.addAttribute("errorMessage", "Dogodila se pogreška na poslužitelju.");
		return "error/error";
	}

}
