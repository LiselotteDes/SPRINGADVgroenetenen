package be.vdab.groenetenen.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
}
