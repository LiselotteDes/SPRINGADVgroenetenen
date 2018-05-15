package be.vdab.groenetenen.restservices;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

import be.vdab.groenetenen.entities.Filiaal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class FilialenResource extends ResourceSupport {
	
	/*
	 * Standaard zou het XML element dat één filiaal voorstelt filialenIdNaam heten, gebaseerd op de naam van de private variabele.
	 * Met deze annotation wijzig je deze naam naar filiaal.
	 */
	@XmlElement(name = "filiaal")
	/*
	 * Standaard zou de JSON property die de verzameling filialen voorstelt filialenIdNaam heten.
	 * Met deze annotation wijzig je deze naam naar filialen.
	 */
	@JsonProperty("filialen")
	private final List<FiliaalIdNaam> filalenIdNaam = new ArrayList<>();
	
	FilialenResource() {} 		// JAXB heeft een default constructor nodig
	
	FilialenResource(Iterable<Filiaal> filialen, EntityLinks entityLinks) {
		for (Filiaal filiaal : filialen) {
			/*Je voegt per filiaal een item toe met het id en de naam van het filiaal.*/
			this.filalenIdNaam.add(new FiliaalIdNaam(filiaal));
			/*Je voegt per filiaal een link met de URL van dat filiaal toe.
			  Je vult het rel attribuut van deze link met 'detail.', gevolgd door het filiaalnummer.*/
			this.add(entityLinks.linkToSingleResource(Filiaal.class, filiaal.getId()).withRel("detail." + filiaal.getId()));
		}
		/*Je voegt aan de response een link toe naar alle filialen.*/
		this.add(entityLinks.linkToCollectionResource(Filiaal.class));
	}
}
