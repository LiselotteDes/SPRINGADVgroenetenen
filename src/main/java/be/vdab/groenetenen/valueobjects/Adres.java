package be.vdab.groenetenen.valueobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import be.vdab.groenetenen.constraints.Postcode;


/*
 * "Niet-browser clients rest requests"
 * @XmlAccessorType(XmlAccessType.FIELD):
 * JAXB converteert standaard een object van en naar XML met getters en setters.
 * Met deze annotation heeft JAXB geen getters of setters nodig.
 * Zo blijft de value object class Adres immutable (geen setters).
 * 
 * @JsonAutoDetect(fieldVisibility=Visibility.ANY):
 * Jackson converteert standaard en object van en naar JSON met getters en setters.
 * Met deze annotation heeft Jackson geen getters of setters nodig.
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility=Visibility.ANY)
public class Adres implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotBlank
	@SafeHtml
	private String straat;
	@NotBlank
	@SafeHtml
	private String huisNr;
	@NotNull
	/*@Range(min = 1000, max = 9999)*/
	@Postcode
	private int postcode;
	@NotBlank
	@SafeHtml
	private String gemeente;
	
	public Adres() {
	}
	public Adres(String straat, String huisNr, int postcode, String gemeente) {
		this.straat = straat;
		this.huisNr = huisNr;
		this.postcode = postcode;
		this.gemeente = gemeente;
	}
	
	public String getStraat() {
		return straat;
	}
	public String getHuisNr() {
		return huisNr;
	}
	public int getPostcode() {
		return postcode;
	}
	public String getGemeente() {
		return gemeente;
	}
}
