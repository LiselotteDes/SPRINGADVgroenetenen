package be.vdab.groenetenen.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import be.vdab.groenetenen.entities.Werknemer;

public interface WerknemerService {
	
	//List<Werknemer> findAll();
	Page<Werknemer> findAll(Pageable pageable);		// "Pagineren"
}
