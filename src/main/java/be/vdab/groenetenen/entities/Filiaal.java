package be.vdab.groenetenen.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

import be.vdab.groenetenen.adapters.LocalDateAdapter;
import be.vdab.groenetenen.valueobjects.Adres;

/*
 * "Niet-browser clients rest requests"
 * @XmlRootElement: JAXB vereist dat je dit schrijft bij een class die het root eleement voorstelt in XML.
 * 					De naam van dit root element is gelijk aan de naam van de class, met de eerste letter in kleine letters: <filiaal>.
 * @XmlAccessorType(AccessType.FIELD): JAXB converteert standaard een object van en naar XML met getters en setters.
 * 										Met deze annotation heeft JAXB geen getters of setters nodig. Zo hoef je geen setId method te schrijven.
 * @JsonAutoDetect(fieldVisibility=Visibility.ANY): Jackson converteert standaard een object van en naar JSON met getters en setters;
 * 													Met deze annotatie heeft Jackson geen getters of setters nodig.
 */
@Entity
@Table(name = "filialen")
@XmlRootElement	
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility=Visibility.ANY)
public class Filiaal implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank
	@SafeHtml
	private String naam;
	private boolean hoofdFiliaal;
	@NumberFormat(style = Style.NUMBER)
	@NotNull
	@Min(0)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal waardeGebouw;
	/*
	 * "Niet-browser clients rest requests": @XmlJavaTypeAdapter
	 * Je geeft hiermee de class aan waarmee JAXB de LocalDate in deze variabele kan converteren van/naar een String.
	 */
	@DateTimeFormat(style = "S-")
	@NotNull
	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate inGebruikName;
	@Valid
	@Embedded
	private Adres adres;
	@Version
	private long versie;

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}

	public boolean isHoofdFiliaal() {
		return hoofdFiliaal;
	}
	public void setHoofdFiliaal(boolean hoofdFiliaal) {
		this.hoofdFiliaal = hoofdFiliaal;
	}
	
	public BigDecimal getWaardeGebouw() {
		return waardeGebouw;
	}
	public void setWaardeGebouw(BigDecimal waardeGebouw) {
		this.waardeGebouw = waardeGebouw;
	}
	
	public LocalDate getInGebruikName() {
		return inGebruikName;
	}
	public void setInGebruikName(LocalDate inGebruikName) {
		this.inGebruikName = inGebruikName;
	}

	public Adres getAdres() {
		return adres;
	}
	public void setAdres(Adres adres) {
		this.adres = adres;
	}

	public long getVersie() {
		return versie;
	}
	
	/*
	 * "Niet-browser clients rest requests": @XmlTransient en @JsonIgnore
	 * Wanneer Spring een Filiaal object omzet naar XML of naar JSON slaat Spring de werknemer data over. 
	 * Je beperkt zo de omvang van de XML of JSON data.
	 * a. Bij een GET request naar /filialen/1
	 *    bevat de response algemene data (zonder werknemers) van filiaal 1.
	 * b. Pas bij een GET request naar /filialen/1/werknemers
	 *    zal de response de werknemers van het filiaal 1 bevatten.
	 */
	@OneToMany(mappedBy = "filiaal")
	@XmlTransient
	@JsonIgnore
	private Set<Werknemer> werknemers;
	public Set<Werknemer> getWerknemers() {
		return Collections.unmodifiableSet(werknemers);
	}
}