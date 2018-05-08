package be.vdab.groenetenen.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "offertes")
public class Offerte implements Serializable {
	/*
	 * Je definieert een validation group als een lege interface.
	 * De naam van de interface wordt de naam van de validation group.
	 */
	public interface Stap1 {}
	public interface Stap2 {}
	
	private final static long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(groups = Stap1.class)		/*Je bepaalt met groups tot welke validation group de annotation hoort.*/
	@SafeHtml(groups = Stap1.class)
	private String voornaam;
	@NotBlank(groups = Stap1.class)
	@SafeHtml(groups = Stap1.class)
	private String familienaam;
	@NotNull(groups = Stap1.class)
	@Email(groups = Stap1.class)
	private String emailadres;
	@DateTimeFormat(style = "S-")
	private LocalDate aangemaakt = LocalDate.now();
	@NotNull(groups = Stap2.class)
	@Min(value = 1, groups = Stap2.class)
	@NumberFormat
	private Integer oppervlakte;
	
	public String getVoornaam() {
		return voornaam;
	}
	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}
	public String getFamilienaam() {
		return familienaam;
	}
	public void setFamilienaam(String familienaam) {
		this.familienaam = familienaam;
	}
	public String getEmailadres() {
		return emailadres;
	}
	public void setEmailadres(String emailadres) {
		this.emailadres = emailadres;
	}
	public Integer getOppervlakte() {
		return oppervlakte;
	}
	public void setOppervlakte(Integer oppervlakte) {
		this.oppervlakte = oppervlakte;
	}
	public long getId() {
		return id;
	}
	public LocalDate getAangemaakt() {
		return aangemaakt;
	}
}
