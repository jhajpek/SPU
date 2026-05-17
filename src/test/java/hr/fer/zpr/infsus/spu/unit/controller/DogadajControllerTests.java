package hr.fer.zpr.infsus.spu.unit.controller;

import hr.fer.zpr.infsus.spu.controller.DogadajController;
import hr.fer.zpr.infsus.spu.dto.CjenikFormDto;
import hr.fer.zpr.infsus.spu.dto.DogadajDetailsDto;
import hr.fer.zpr.infsus.spu.dto.DogadajFormDto;
import hr.fer.zpr.infsus.spu.model.Dogadaj;
import hr.fer.zpr.infsus.spu.model.Dvorana;
import hr.fer.zpr.infsus.spu.model.Lokacija;
import hr.fer.zpr.infsus.spu.service.DogadajService;
import hr.fer.zpr.infsus.spu.service.DvoranaService;
import hr.fer.zpr.infsus.spu.util.EntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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

@WebMvcTest(DogadajController.class)
public class DogadajControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DvoranaService dvoranaService;

    @MockitoBean
    private DogadajService dogadajService;

    @Test
    public void DogadajController_GetDogadaji_ReturnListView() throws Exception {
        when(dogadajService.search(anyString(), anyString())).thenReturn(List.of());

        mockMvc.perform(get("/dogadaji")
                        .param("naziv", "Koncert")
                        .param("kategorija", "Glazba"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andExpect(model().attributeExists("dvorane"))
                .andExpect(model().attributeExists("dogadaji"))
                .andExpect(model().attributeExists("dvorane"))
                .andExpect(model().attribute("naziv", "Koncert"))
                .andExpect(model().attribute("kategorija", "Glazba"));

        verify(dogadajService, times(1)).search(eq("Koncert"), eq("Glazba"));
    }

    @Test
    public void DogadajController_GetDogadajForm_ReturnFormView() throws Exception {
        mockMvc.perform(get("/dogadaji/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().attributeExists("dvorane"))
                .andExpect(model().attributeExists("dogadaj"));
    }

    @Test
    public void DogadajController_PostDogadaj_ReturnListView() throws Exception {
        mockMvc.perform(post("/dogadaji")
                        .param("naziv", "Dogadaj")
                        .param("kategorija", "Sport")
                        .param("opis", "")
                        .param("datumVrijemeOdrzavanja", "2027-05-05T12:00")
                        .param("dvoranaId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji"))
                .andExpect(flash().attribute("successMessage", "Događaj je uspješno spremljen."));

        verify(dogadajService, times(1)).save(any(DogadajFormDto.class));
    }

    @Test
    public void DogadajController_PostInvalidDogadaj_ReturnFormView() throws Exception {
        mockMvc.perform(post("/dogadaji")
                        .param("naziv", "Dogadaj")
                        .param("kategorija", "")
                        .param("opis", "")
                        .param("datumVrijemeOdrzavanja", "2027-05-05T12:00")
                        .param("dvoranaId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("dvorane"));

        verify(dogadajService, never()).save(any(DogadajFormDto.class));
    }

    @Test
    public void DogadajController_GetDogadajFormToEdit_ReturnFormView() throws Exception {
        when(dogadajService.getFormDtoById(anyLong())).thenReturn(new DogadajFormDto());

        mockMvc.perform(get("/dogadaji/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().attributeExists("dvorane"))
                .andExpect(model().attributeExists("dogadaj"));

        verify(dogadajService, times(1)).getFormDtoById(anyLong());
    }

    @Test
    public void DogadajController_PostDogadajFormToEdit_ReturnListView() throws Exception {
        mockMvc.perform(post("/dogadaji/edit/1")
                        .param("naziv", "Dogadaj")
                        .param("kategorija", "Sport")
                        .param("opis", "")
                        .param("datumVrijemeOdrzavanja", "2027-05-05T12:00")
                        .param("dvoranaId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji"))
                .andExpect(flash().attribute("successMessage", "Događaj je uspješno ažuriran."));

        verify(dogadajService, times(1)).update(eq(1L), any(DogadajFormDto.class));
    }

    @Test
    public void DogadajController_PostInvalidDogadajFormToEdit_ReturnFormView() throws Exception {
        mockMvc.perform(post("/dogadaji/edit/1")
                        .param("naziv", "Dogadaj")
                        .param("kategorija", "Sport")
                        .param("opis", "")
                        .param("datumVrijemeOdrzavanja", "2027-05-05T12-00")
                        .param("dvoranaId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("dvorane"))
                .andExpect(model().attributeExists("dogadaj"));

        verify(dogadajService, never()).update(eq(1L), any(DogadajFormDto.class));
    }

    @Test
    public void DogadajController_PostDogadajFormToEditWhenServiceThrowsIllegalArgumentException_ReturnFormView() throws Exception {
        String errorMessage = "U odabranoj dvorani već postoji događaj u tom terminu.";

        doThrow(new IllegalArgumentException(errorMessage)).when(dogadajService).update(anyLong(), any(DogadajFormDto.class));

        mockMvc.perform(post("/dogadaji/edit/1")
                        .param("naziv", "Dogadaj")
                        .param("kategorija", "Sport")
                        .param("opis", "")
                        .param("datumVrijemeOdrzavanja", "2027-05-05T12:00")
                        .param("dvoranaId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().attributeExists("dogadaj"))
                .andExpect(model().attribute("errorMessage", errorMessage));

        verify(dogadajService, times(1)).update(eq(1L), any(DogadajFormDto.class));
    }

    @Test
    public void DogadajController_DeleteDogadaj_ReturnListView() throws Exception {
        mockMvc.perform(post("/dogadaji/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dogadaji"))
                .andExpect(flash().attribute("successMessage", "Događaj je uspješno izbrisan."));

        verify(dogadajService, times(1)).deleteById(eq(1L));
    }

    @Test
    public void DogadajController_GetDogadaj_ReturnDetailView() throws Exception {
        Lokacija lokacija = EntityFactory.createLokacija();
        Dvorana dvorana  = EntityFactory.createDvorana("Dvorana", lokacija);
        Dogadaj dogadaj = EntityFactory.createDogadaj("Dogadaj", dvorana);
        CjenikFormDto cjenikFormDto = new CjenikFormDto();
        cjenikFormDto.setDogadajId(1L);
        cjenikFormDto.setSektorId(1L);

        DogadajDetailsDto dogadajDetailsDto = new DogadajDetailsDto();
        dogadajDetailsDto.setDogadaj(dogadaj);
        dogadajDetailsDto.setCjenici(new ArrayList<>());
        dogadajDetailsDto.setDostupniSektori(new ArrayList<>());
        dogadajDetailsDto.setProdaneUlaznice(new HashMap<>());
        dogadajDetailsDto.setNoviCjenik(cjenikFormDto);

        when(dogadajService.getDetailsById(anyLong())).thenReturn(dogadajDetailsDto);

        mockMvc.perform(get("/dogadaji/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/detail"))
                .andExpect(model().attributeExists("dvorane"))
                .andExpect(model().attributeExists("details"))
                .andExpect(model().attributeExists("noviCjenik"));

        verify(dogadajService, times(1)).getDetailsById(eq(1L));
    }

}
