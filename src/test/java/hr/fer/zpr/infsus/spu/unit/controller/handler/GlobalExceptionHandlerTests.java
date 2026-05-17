package hr.fer.zpr.infsus.spu.unit.controller.handler;

import hr.fer.zpr.infsus.spu.controller.DogadajController;
import hr.fer.zpr.infsus.spu.service.DogadajService;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DogadajController.class)
public class GlobalExceptionHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DvoranaService dvoranaService;

    @MockitoBean
    private DogadajService dogadajService;

    @Test
    public void GlobalExceptionHandler_HandleNoResourceFoundException_ReturnErrorView() throws Exception {
        mockMvc.perform(get("/blabla"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("errorMessage", "Stranica koju tražite ne postoji."));
    }

    @Test
    public void GlobalExceptionHandler_HandleIllegalArgumentException_ReturnRefererView() throws Exception {
        when(dogadajService.getDetailsById(1L)).thenThrow(new IllegalArgumentException("Događaj s ID 1 ne postoji."));

        mockMvc.perform(get("/dogadaji/1")
                        .header("Referer", "/dogadaji"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji"))
                .andExpect(flash().attribute("errorMessage", "Događaj s ID 1 ne postoji."));
    }

    @Test
    public void GlobalExceptionHandler_HandleGeneralException_ReturnErrorView() throws Exception {
        when(dogadajService.getDetailsById(anyLong())).thenThrow(new RuntimeException("500 - Internal server error."));

        mockMvc.perform(get("/dogadaji/1"))
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("errorMessage", "Dogodila se pogreška na poslužitelju."));
    }

}
