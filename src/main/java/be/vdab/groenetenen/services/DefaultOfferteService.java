package be.vdab.groenetenen.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import be.vdab.groenetenen.entities.Offerte;
import be.vdab.groenetenen.mail.MailSender;
import be.vdab.groenetenen.messaging.OfferteEnOffertesURL;
import be.vdab.groenetenen.repositories.OfferteRepository;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
class DefaultOfferteService implements OfferteService {
	
	private final OfferteRepository offerteRepository;
	private final MailSender mailSender;
	
	// [Java Messaging System]
	/*Je stuurt een message naar de message broker met de class JmsTemplate. Spring maakt bij de start van de website zelf een bean van deze class.*/
	private final JmsTemplate jmsTemplate;
	/*Deze variabele zal de naam bevatten van de queue waarnaar je messages stuurt.*/
	private final String nieuweOfferteQueue;
	
	DefaultOfferteService(OfferteRepository offerteRepository, MailSender mailSender, 
							JmsTemplate jmsTemplate, @Value("${nieuweOfferteQueue}") String nieuweOfferteQueue) {
		this.offerteRepository = offerteRepository;
		this.mailSender = mailSender;
		this.jmsTemplate = jmsTemplate;
		this.nieuweOfferteQueue = nieuweOfferteQueue;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void create(Offerte offerte, String offertesURL) {
		
		offerteRepository.save(offerte);
		// mailSender.nieuweOfferte(offerte, offertesURL);
		
		// [Java Messaging System]
		/*Je maakt een object dat je zal opnemen in de message die je verstuurt.*/
		OfferteEnOffertesURL offerteEnOffertesURL = new OfferteEnOffertesURL(offerte, offertesURL);
		/*
		 * Je converteert het OfferteEnOffertesURL object.
		 * Spring gebruikt daarbij de MarshallingMessageConverter bean die je definieerde. Spring converteert daarmee het object naar XML.
		 * Je verstuurt daarna deze XML naar de message broker queue nieuweOfferteQueue.
		 */
		jmsTemplate.convertAndSend(nieuweOfferteQueue, offerteEnOffertesURL);
		
	}
	
	@Override
	public Optional<Offerte> read(long id) {
		return offerteRepository.findById(id);
	}
	
	@Override
//	@Scheduled(/*cron = "0 0/1 * 1/1 * ?"*/ fixedRate = 60000)	// test = om de minuut
	public void aantalOffertesMail() {
		mailSender.aantalOffertesMail(offerteRepository.count());
	}
}
