package be.vdab.groenetenen.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import be.vdab.groenetenen.entities.Werknemer;

public interface WerknemerRepository extends JpaRepository<Werknemer, Long> {
	
	@Override								// Je overschrijft een method uit je base interface (JpaRepository).
	@EntityGraph(Werknemer.MET_FILIAAL)		// Dit overschrijven is het toepassen van de named entity graph Werknemer.metFiliaal
	List<Werknemer> findAll(Sort sort);		// Dit is de method declaratie zoals beschreven in de base interface.
	
}
