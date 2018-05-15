package be.vdab.groenetenen.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import be.vdab.groenetenen.entities.Filiaal;
import be.vdab.groenetenen.exceptions.FiliaalHeeftNogWerknemersException;
import be.vdab.groenetenen.repositories.FiliaalRepository;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
class DefaultFiliaalService implements FiliaalService {
	private final FiliaalRepository filiaalRepository;
		
	DefaultFiliaalService(FiliaalRepository repository) {
		this.filiaalRepository = repository;
	}
	
	@Override
	public List<Filiaal> findByPostcode(int van, int tot) {
		return filiaalRepository.findByAdresPostcodeBetweenOrderByAdresPostcode(van, tot);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void delete(long id) {
		Optional<Filiaal> optionalFiliaal = filiaalRepository.findById(id);
		if (optionalFiliaal.isPresent()) {
			if (! optionalFiliaal.get().getWerknemers().isEmpty()) {
				throw new FiliaalHeeftNogWerknemersException();
			}
			filiaalRepository.deleteById(id);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void create(Filiaal filiaal) {
		filiaalRepository.save(filiaal);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void update(Filiaal filiaal) {
		filiaalRepository.save(filiaal);
	}
	
	@Override
	public List<Filiaal> findAll() {
		return filiaalRepository.findAll();
	}
}
