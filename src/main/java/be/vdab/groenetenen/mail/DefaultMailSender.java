package be.vdab.groenetenen.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import be.vdab.groenetenen.entities.Offerte;
import be.vdab.groenetenen.exceptions.KanMailNietZendenException;

@Component
class DefaultMailSender implements MailSender {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultMailSender.class);
	private final JavaMailSender sender;
	private final String emailAdresWebMaster;
	
	/*Spring maakt een bean die de interface JavaMailSender implementeert en initialiseert deze class met de mail eigenschappen uit application.properties.
	 *Als je in jouw code een mail wil versturen injecteer je deze bean in je code. De bean helpt je een mail te versturen.*/
	DefaultMailSender(JavaMailSender sender, @Value("${emailAdresWebMaster}") String emailAdresWebMaster) {
		
		this.sender = sender;
		this.emailAdresWebMaster = emailAdresWebMaster;
		
	}

	@Override
	// @Async	// Je verstuurt de mail (trage operatie) in een aparte thread. De oorspronkelijke thread stuurt intussen een response naar de browser.
				/* [Java Messagin System]: @Async mag je verwijderen: 
				 * het verwerken van messages van een message broker gebeurt sowieso in een achtergrondtaak. */
	public void nieuweOfferte(Offerte offerte, String offertesURL) {
		
		try {
			// SimpleMailMessage message = new SimpleMailMessage();		// SimpleMailMessage stelt een email zonder opmaak voor.
			MimeMessage message = sender.createMimeMessage();			// MimeMessage stelt een email met HTML opmaak voor.
			MimeMessageHelper helper = new MimeMessageHelper(message);	// MimeMessageHalper helpt je de eigenschappen van de email in te stellen.
			
			// message.setTo(offerte.getEmailadres());					// Je vult de eigenschappen van de email in.
			// message.setSubject("Nieuwe offerte");
			// message.setText("Uw offerte heeft het nummer " + offerte.getId());
			
			helper.setTo(offerte.getEmailadres());						// Je stelt de eigenschappen van de email in.
			helper.setSubject("Nieuwe offerte");
			/*Je maakt de URL van de nieuwe offerte. Dit is de URL localhost:8080/offertes/ waaraan je het offertenummer toevoegt.*/
			String offerteURL = offertesURL + offerte.getId();
			helper.setText("Uw offerte heeft het nummer <strong>"
								+ offerte.getId() 
								+ "</strong>. Je vindt hier de offerte <a href='"
								+ offerteURL
								+ "'>hier</a>.", 
							true);										// Je gebruikt HTML tags (<strong>,<a>). Je moet de 2° parameter dan op true plaatsen.
			
			sender.send(message);										// Je verstuurt de email.
			
		} catch (MailException | MessagingException ex) {
			LOGGER.error("Kan mail nieuwe offerte niet versturen", ex);
			throw new KanMailNietZendenException();
		}
		
	}
	
	@Override
	public void aantalOffertesMail(long aantal) {
		
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setTo(emailAdresWebMaster);
			helper.setSubject("Aantal offertes");
			helper.setText("Aantal offertes: <strong>" + aantal + "</strong>", true);
			sender.send(message);
			
		} catch (MailException | MessagingException ex) {
			
			LOGGER.error("Kan mail aantal offertes niet versturen", ex);
			throw new KanMailNietZendenException();
			
		}
	}
}
