package be.vdab.groenetenen.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import be.vdab.groenetenen.entities.Offerte;
import be.vdab.groenetenen.mail.MailSender;
import be.vdab.groenetenen.repositories.OfferteRepository;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
class DefaultOfferteService implements OfferteService {
	
	private final OfferteRepository offerteRepository;
	private final MailSender mailSender;
	
	DefaultOfferteService(OfferteRepository offerteRepository, MailSender mailSender) {
		this.offerteRepository = offerteRepository;
		this.mailSender = mailSender;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void create(Offerte offerte, String offertesURL) {
		offerteRepository.save(offerte);
		mailSender.nieuweOfferte(offerte, offertesURL);
	}
	
	@Override
	public Optional<Offerte> read(long id) {
		return offerteRepository.findById(id);
	}
}
