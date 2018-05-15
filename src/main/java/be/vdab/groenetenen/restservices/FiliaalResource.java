package be.vdab.groenetenen.restservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import be.vdab.groenetenen.entities.Filiaal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class FiliaalResource extends ResourceSupport {
	
	/*Je erft van de class ResourceSupport en je hebt zo een verzameling Link objecten. */
	
	/*Je leest de variabele filiaal nergens in je code. Je vermijdt hier de warning die de compiler daarbij kan geven. */
	@SuppressWarnings("unused")
	/*Je neemt een Filiaal object op. De XML voorstelling hiervan wordt dan een onderdeel van de XML voorstelling van een FiliaalResource.*/
	private Filiaal filiaal;
	
	FiliaalResource() {} 	// JAXB heeft een default constructor nodig
	
	FiliaalResource(Filiaal filiaal, EntityLinks entityLinks) {
		this.filiaal = filiaal;
		/*Je voegt aan de verzamelink Link objecten een link toe naar het filiaal zelf. 
		 * De XML voorstelling van een FiliaalResource object zal ook deze link bevatten.*/
		this.add(entityLinks.linkToSingleResource(Filiaal.class, filiaal.getId()));
		/*Je voegt aan de verzameling Link objecten een link toe naar de werknemers van het filiaal.
		 * De XML voorstelling van een FiliaalResource object zal ook deze link bevatten.
		 * Je bouwt de link op als de link naar het filiaal zelf, een slash en het woord werknemers.*/
		this.add(entityLinks.linkForSingleResource(Filiaal.class, filiaal.getId()).slash("werknemers").withRel("werknemers"));
	}
}
