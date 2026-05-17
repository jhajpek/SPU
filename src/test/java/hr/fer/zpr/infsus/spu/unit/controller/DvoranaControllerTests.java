package hr.fer.zpr.infsus.spu.unit.controller;

import hr.fer.zpr.infsus.spu.controller.DvoranaController;
import hr.fer.zpr.infsus.spu.dto.DvoranaFormDto;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DvoranaController.class)
public class DvoranaControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DvoranaService dvoranaService;

	@Test
	public void DvoranaController_GetDvorane_ReturnListView() throws Exception {
		when(dvoranaService.search(anyString())).thenReturn(List.of());

		mockMvc.perform(get("/dvorane").param("query", "Arena")).andExpect(status().isOk())
				.andExpect(view().name("halls/list")).andExpect(model().attributeExists("dvorane"))
				.andExpect(model().attribute("query", "Arena"));

		verify(dvoranaService, times(1)).search(eq("Arena"));
	}

	@Test
	public void DvoranaController_GetDvoranaForm_ReturnFormView() throws Exception {
		when(dvoranaService.findAllUnusedLokacije(null)).thenReturn(List.of());

		mockMvc.perform(get("/dvorane/new")).andExpect(status().isOk()).andExpect(view().name("halls/form"))
				.andExpect(model().attributeExists("dvorana")).andExpect(model().attributeExists("lokacije"));

		verify(dvoranaService, times(1)).findAllUnusedLokacije(null);
	}

	@Test
	public void DvoranaController_PostDvorana_ReturnListView() throws Exception {
		mockMvc.perform(post("/dvorane").param("naziv", "Dvorana").param("lokacijaId", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dvorane"))
				.andExpect(flash().attribute("successMessage", "Dvorana je uspješno spremljena."));

		verify(dvoranaService, times(1)).save(any(DvoranaFormDto.class));
	}

	@Test
	public void DvoranaController_PostInvalidDvorana_ReturnFormView() throws Exception {
		when(dvoranaService.findAllUnusedLokacije(1L)).thenReturn(List.of());

		mockMvc.perform(post("/dvorane").param("naziv", "").param("lokacijaId", "1")).andExpect(status().isOk())
				.andExpect(view().name("halls/form")).andExpect(model().hasErrors())
				.andExpect(model().attributeExists("lokacije"));

		verify(dvoranaService, times(1)).findAllUnusedLokacije(1L);
		verify(dvoranaService, never()).save(any(DvoranaFormDto.class));
	}

	@Test
	public void DvoranaController_GetDvoranaFormToEdit_ReturnFormView() throws Exception {
		DvoranaFormDto dto = new DvoranaFormDto();
		dto.setLokacijaId(1L);
		when(dvoranaService.getFormDtoById(anyLong())).thenReturn(dto);
		when(dvoranaService.findAllUnusedLokacije(eq(1L))).thenReturn(List.of());

		mockMvc.perform(get("/dvorane/edit/1")).andExpect(status().isOk()).andExpect(view().name("halls/form"))
				.andExpect(model().attributeExists("dvorana")).andExpect(model().attributeExists("lokacije"));

		verify(dvoranaService, times(1)).getFormDtoById(eq(1L));
		verify(dvoranaService, times(1)).findAllUnusedLokacije(eq(1L));
	}

	@Test
	public void DvoranaController_PostDvoranaFormToEdit_ReturnListView() throws Exception {
		mockMvc.perform(post("/dvorane/edit/1").param("naziv", "Dvorana").param("lokacijaId", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dvorane"))
				.andExpect(flash().attribute("successMessage", "Dvorana je uspješno spremljena."));

		verify(dvoranaService, times(1)).update(eq(1L), any(DvoranaFormDto.class));
	}

	@Test
	public void DvoranaController_PostInvalidDvoranaFormToEdit_ReturnFormView() throws Exception {
		when(dvoranaService.findAllUnusedLokacije(1L)).thenReturn(List.of());

		mockMvc.perform(post("/dvorane/edit/1").param("naziv", "").param("lokacijaId", "1")).andExpect(status().isOk())
				.andExpect(view().name("halls/form")).andExpect(model().hasErrors())
				.andExpect(model().attributeExists("lokacije"));

		verify(dvoranaService, times(1)).findAllUnusedLokacije(1L);
		verify(dvoranaService, never()).update(eq(1L), any(DvoranaFormDto.class));
	}

	@Test
	public void DvoranaController_DeleteDvorana_ReturnListView() throws Exception {
		mockMvc.perform(post("/dvorane/delete/1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dvorane"))
				.andExpect(flash().attribute("successMessage", "Dvorana je uspješno obrisana."));

		verify(dvoranaService, times(1)).deleteById(eq(1L));
	}

}
