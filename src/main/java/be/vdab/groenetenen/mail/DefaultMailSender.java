package be.vdab.groenetenen.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import be.vdab.groenetenen.entities.Offerte;
import be.vdab.groenetenen.exceptions.KanMailNietZendenException;

@Component
class DefaultMailSender implements MailSender {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultMailSender.class);
	private final JavaMailSender sender;
	/*Spring maakt een bean die de interface JavaMailSender implementeert en initialiseert deze class met de mail eigenschappen uit application.properties.
	 *Als je in jouw code een mail wil versturen injecteer je deze bean in je code. De bean helpt je een mail te versturen.*/
	DefaultMailSender(JavaMailSender sender) {
		this.sender = sender;
	}

	@Override
	public void nieuweOfferte(Offerte offerte) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();	// SimpleMailMessage stelt een email zonder opmaak voor.
			message.setTo(offerte.getEmailadres());					// Je vult de eigenschappen van de email in.
			message.setSubject("Nieuwe offerte");
			message.setText("Uw offerte heeft het nummer " + offerte.getId());
			sender.send(message);									// Je verstuurt de email.
		} catch (MailException ex) {
			LOGGER.error("Kan mail nieuwe offerte niet versturen", ex);
			throw new KanMailNietZendenException();
		}
	}
}
