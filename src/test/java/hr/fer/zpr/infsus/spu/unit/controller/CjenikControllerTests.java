package hr.fer.zpr.infsus.spu.unit.controller;

import hr.fer.zpr.infsus.spu.controller.CjenikController;
import hr.fer.zpr.infsus.spu.dto.CjenikFormDto;
import hr.fer.zpr.infsus.spu.service.CjenikService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CjenikController.class)
public class CjenikControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CjenikService cjenikService;

    @Test
    public void CjenikController_PostCjenik_ReturnDetailView() throws Exception {
        mockMvc.perform(post("/cjenici")
                        .param("dogadajId", "1")
                        .param("sektorId", "1")
                        .param("cijena", "30.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji/1"))
                .andExpect(flash().attribute("successMessage", "Sektor je uspješno dodan."));

        verify(cjenikService, times(1)).save(any(CjenikFormDto.class));
    }

    @Test
    public void CjenikController_PostInvalidCjenik_ReturnDetailView() throws Exception {
        mockMvc.perform(post("/cjenici")
                        .param("dogadajId", "1")
                        .param("sektorId", "1")
                        .param("cijena", "-30.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji/1"))
                .andExpect(flash().attribute("errorMessage", "Neispravni podaci za cjenik."));

        verify(cjenikService, never()).save(any(CjenikFormDto.class));
    }

    @Test
    public void CjenikController_PostCjenikFormToEdit_ReturnDetailView() throws Exception {
        mockMvc.perform(post("/cjenici/edit/1")
                        .param("dogadajId", "1")
                        .param("sektorId", "1")
                        .param("cijena", "30.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji/1"))
                .andExpect(flash().attribute("successMessage", "Cijena je uspješno ažurirana."));

        verify(cjenikService, times(1)).update(eq(1L), any(CjenikFormDto.class));
    }

    @Test
    public void CjenikController_PostInvalidCjenikFormToEdit_ReturnDetailView() throws Exception {
        mockMvc.perform(post("/cjenici/edit/1")
                        .param("dogadajId", "1")
                        .param("sektorId", "1")
                        .param("cijena", "-30.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji/1"))
                .andExpect(flash().attribute("errorMessage", "Neispravni podaci za cjenik."));

        verify(cjenikService, never()).update(eq(1L), any(CjenikFormDto.class));
    }

    @Test
    public void CjenikController_DeleteCjenik_ReturnDetailView() throws Exception {
        mockMvc.perform(post("/cjenici/delete/1")
                        .param("dogadajId", "1")
                        .param("sektorId", "1")
                        .param("cijena", "-30.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji/1"))
                .andExpect(flash().attribute("successMessage", "Sektor je uklonjen s događaja."));

        verify(cjenikService, times(1)).deleteById(eq(1L));
    }

}
