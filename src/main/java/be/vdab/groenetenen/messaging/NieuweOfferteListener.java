package be.vdab.groenetenen.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import be.vdab.groenetenen.mail.MailSender;

@Component
class NieuweOfferteListener {
	
	private final MailSender mailSender;
	
	NieuweOfferteListener(MailSender mailSender) {	// Je injecteert de bean waarmee je emails kan versturen
		this.mailSender = mailSender;
	}

	/*
	 * Je tikt @JmsListener voor een method waarin je messages van de message broker wil ontvangen.
	 * Je duidt daarbij de queue aan waaruit je boodschappen wil ontvangen.
	 * Je tikt de naam van deze queue niet letterlijk (hard coded) maar verwijst naar de instelling met de naam nieuweOfferteQueue in application.properties.
	 */
	@JmsListener(destination = "${nieuweOfferteQueue}")
	/*
	 * Je geeft de method een parameter van het type OfferteEnOffertesURL.
	 * Telkens de method een boodschap van de message broker ontvangt, converteert Spring de XML in deze boodschap naar een OfferteEnOffertesURl object
	 * en biedt het aan in deze parameter.
	 */
	void ontvangBoodschap(OfferteEnOffertesURL offerteEnOffertesURL) {
		
		/*Je verwerkt de boodschap. Je stuurt een email naar het email adres in de offerte.*/
		mailSender.nieuweOfferte(offerteEnOffertesURL.getOfferte(), offerteEnOffertesURL.getOffertesURL());
	}
}
