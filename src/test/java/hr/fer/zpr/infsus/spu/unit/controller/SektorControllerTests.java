package hr.fer.zpr.infsus.spu.unit.controller;

import hr.fer.zpr.infsus.spu.controller.SektorController;
import hr.fer.zpr.infsus.spu.dto.SektorFormDto;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import hr.fer.zpr.infsus.spu.service.SektorService;
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

@WebMvcTest(SektorController.class)
public class SektorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DvoranaService dvoranaService;
    
    @MockitoBean
    private SektorService sektorService;

    @Test
    public void SektorController_GetSektori_ReturnListView() throws Exception {
        when(sektorService.search(anyString(), anyLong())).thenReturn(List.of());
        when(dvoranaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/sektori")
                        .param("naziv", "VIP")
                        .param("dvoranaId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("sectors/list"))
                .andExpect(model().attributeExists("sektori"))
                .andExpect(model().attributeExists("dvorane"))
                .andExpect(model().attribute("naziv", "VIP"))
                .andExpect(model().attribute("dvoranaId", 1L));

        verify(sektorService, times(1)).search(eq("VIP"), eq(1L));
        verify(dvoranaService, times(1)).findAll();
    }

    @Test
    public void SektorController_GetSektorForm_ReturnFormView() throws Exception {
        when(dvoranaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/sektori/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("sectors/form"))
                .andExpect(model().attributeExists("sektor"))
                .andExpect(model().attributeExists("dvorane"));

        verify(dvoranaService, times(1)).findAll();
    }

    @Test
    public void SektorController_PostSektor_ReturnListView() throws Exception {
        mockMvc.perform(post("/sektori")
                        .param("naziv", "VIP")
                        .param("kapacitet", "100")
                        .param("dvoranaId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sektori"))
                .andExpect(flash().attribute("successMessage", "Sektor je uspješno spremljen."));

        verify(sektorService, times(1)).save(any(SektorFormDto.class));
    }

    @Test
    public void SektorController_PostInvalidSektor_ReturnFormView() throws Exception {
        when(dvoranaService.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/sektori")
                        .param("naziv", "VIP")
                        .param("kapacitet", "0")
                        .param("dvoranaId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("sectors/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("dvorane"));

        verify(dvoranaService, times(1)).findAll();
        verify(sektorService, never()).save(any(SektorFormDto.class));
    }

    @Test
    public void SektorController_GetSektorFormToEdit_ReturnFormView() throws Exception {
        when(sektorService.getFormDtoById(1L)).thenReturn(new SektorFormDto());
        when(dvoranaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/sektori/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("sectors/form"))
                .andExpect(model().attributeExists("sektor"))
                .andExpect(model().attributeExists("dvorane"));

        verify(sektorService, times(1)).getFormDtoById(anyLong());
        verify(dvoranaService, times(1)).findAll();
    }

    @Test
    public void SektorController_PostSektorFormToEdit_ReturnListView() throws Exception {
        mockMvc.perform(post("/sektori/edit/1")
                        .param("naziv", "VIP")
                        .param("kapacitet", "100")
                        .param("dvoranaId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sektori"))
                .andExpect(flash().attribute("successMessage", "Sektor je uspješno ažuriran."));

        verify(sektorService, times(1)).update(eq(1L), any(SektorFormDto.class));
    }

    @Test
    public void SektorController_PostInvalidSektorFormToEdit_ReturnListView() throws Exception {
        when(dvoranaService.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/sektori/edit/1")
                        .param("naziv", "VIP")
                        .param("kapacitet", "0")
                        .param("dvoranaId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("sectors/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("dvorane"));

        verify(dvoranaService, times(1)).findAll();
    }

    @Test
    public void SektorController_DeleteSektor_ReturnListView() throws Exception {
        mockMvc.perform(post("/sektori/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sektori"))
                .andExpect(flash().attribute("successMessage", "Sektor je uspješno obrisan."));

        verify(sektorService, times(1)).deleteById(eq(1L));
    }
    
}
