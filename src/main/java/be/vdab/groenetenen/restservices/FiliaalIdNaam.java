package be.vdab.groenetenen.restservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import be.vdab.groenetenen.entities.Filiaal;

/*
 * Deze class stelt 1 regel uit de response xml voor.
 * Bijvoorbeeld, als het filiaal met id 1 is: <filiaal id="1" naam="Andros"/>
 */

@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class FiliaalIdNaam {
	
	@XmlAttribute		// Met deze annotation wordt id een XML attribuut in plaats van een XML element.
	private long id;
	@XmlAttribute
	private String naam;
	
	FiliaalIdNaam() {} 	// JAXB heeft een default constructor nodig
	
	FiliaalIdNaam(Filiaal filiaal) {
		this.id = filiaal.getId();
		this.naam = filiaal.getNaam();
	}
}
