package hr.fer.zpr.infsus.spu.unit.service;

import hr.fer.zpr.infsus.spu.model.Cjenik;
import hr.fer.zpr.infsus.spu.model.Sektor;
import hr.fer.zpr.infsus.spu.repository.CjenikRepository;
import hr.fer.zpr.infsus.spu.repository.UlaznicaRepository;
import hr.fer.zpr.infsus.spu.service.impl.UlaznicaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UlaznicaServiceTests {

    @Mock
    private UlaznicaRepository ulaznicaRepository;

    @Mock
    private CjenikRepository cjenikRepository;

    @InjectMocks
    private UlaznicaServiceImpl ulaznicaServiceImpl;

    @Test
    public void UlaznicaService_CountUlaznice_ReturnUlazniceCount() {
        Long dogadajId = 1L;
        Long sektorId = 1L;

        when(ulaznicaRepository.countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(dogadajId, sektorId)).thenReturn(5L);

        long numberOfUlaznice = ulaznicaServiceImpl.countSoldTickets(dogadajId, sektorId);

        Assertions.assertEquals(5L, numberOfUlaznice);
        verify(ulaznicaRepository, times(1)).countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(anyLong(), anyLong());
    }

    @Test
    public void UlaznicaService_GetSoldTicketsBySektor_ReturnSektorCountMap() {
        Long dogadajId = 1L;

        Sektor sektor1 = new Sektor();
        sektor1.setSektorId(1L);

        Sektor sektor2 = new Sektor();
        sektor2.setSektorId(2L);

        Cjenik cjenik1 = new Cjenik();
        cjenik1.setSektor(sektor1);

        Cjenik cjenik2 = new Cjenik();
        cjenik2.setSektor(sektor2);

        when(cjenikRepository.findAllByDogadaj_DogadajIdOrderByCijena(dogadajId)).thenReturn(List.of(cjenik1, cjenik2));
        when(ulaznicaRepository.countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(dogadajId, 1L)).thenReturn(20L);
        when(ulaznicaRepository.countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(dogadajId, 2L)).thenReturn(25L);

        Map<Long, Long> sektorCountMap = ulaznicaServiceImpl.getSoldTicketsBySektor(dogadajId);

        Assertions.assertNotNull(sektorCountMap);
        Assertions.assertEquals(2, sektorCountMap.size());
        Assertions.assertEquals(20L, sektorCountMap.get(1L));
        Assertions.assertEquals(25L, sektorCountMap.get(2L));

        verify(cjenikRepository, times(1)).findAllByDogadaj_DogadajIdOrderByCijena(anyLong());
        verify(ulaznicaRepository, times(2)).countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(anyLong(), anyLong());
    }

    @Test
    public void UlaznicaService_GetSoldTicketsBySektor_ReturnEmptySektorCountMap() {
        Long dogadajId = 1L;

        when(cjenikRepository.findAllByDogadaj_DogadajIdOrderByCijena(dogadajId)).thenReturn(List.of());

        Map<Long, Long> sektorCountMap = ulaznicaServiceImpl.getSoldTicketsBySektor(dogadajId);

        Assertions.assertTrue(sektorCountMap.isEmpty());
        verify(cjenikRepository, times(1)).findAllByDogadaj_DogadajIdOrderByCijena(anyLong());
        verify(ulaznicaRepository, never()).countByDogadaj_DogadajIdAndSjedalo_Sektor_SektorId(anyLong(), anyLong());
    }

}
