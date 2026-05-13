package hr.fer.zpr.infsus.spu_backend.service;

import java.util.Map;

public interface UlaznicaService {

	long countSoldTickets(Long dogadajId, Long sektorId);

	Map<Long, Long> getSoldTicketsBySektor(Long dogadajId);

}
