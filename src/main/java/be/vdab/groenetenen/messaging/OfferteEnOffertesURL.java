package be.vdab.groenetenen.messaging;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import be.vdab.groenetenen.entities.Offerte;

/* Je zal boodschappen met objecten van de huidige class naar de message broker sturen.
 * Spring moet die objecten eerst omzetten naar XML. Spring gebruikt hierbij JAXB.
 * JAXB stelt als voorwaarde dat er @XmlRootElement staat voor een claszs waarvan de objecten naar XML worden omgezet.
 */
@XmlRootElement
/* De class bevat geen setters. Met volgende regel benadert JAXB de private variabelen rechtstreeks, zonder tussenkomst van getters en setters.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferteEnOffertesURL {
	
	private Offerte offerte;
	private String offertesURL;
	
	public OfferteEnOffertesURL(Offerte offerte, String offertesURL) {
		this.offerte = offerte;
		this.offertesURL = offertesURL;
	}
	
	protected OfferteEnOffertesURL() {
		// de protected default constructor die JAXB nodig heeft
	}	

	public Offerte getOfferte() {
		return offerte;
	}

	public String getOffertesURL() {
		return offertesURL;
	}

}
