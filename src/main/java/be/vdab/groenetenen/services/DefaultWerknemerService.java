package be.vdab.groenetenen.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import be.vdab.groenetenen.entities.Werknemer;
import be.vdab.groenetenen.repositories.WerknemerRepository;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
class DefaultWerknemerService implements WerknemerService {
	
	private final WerknemerRepository werknemerRepository;
	
	DefaultWerknemerService(WerknemerRepository werknemerRepository) {
		this.werknemerRepository = werknemerRepository;
	}
	
	@Override
	/*public List<Werknemer> findAll() {
		// De by method aanvaardt een variabel aantal eigenschappen waarop Spring sorteert.
		return werknemerRepository.findAll(Sort.by("familienaam", "voornaam"));
	}*/
	// "Pagineren"
	public Page<Werknemer> findAll(Pageable pageable) {
		return werknemerRepository.findAll(pageable);
	}
}
