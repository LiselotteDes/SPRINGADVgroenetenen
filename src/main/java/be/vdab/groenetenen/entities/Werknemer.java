package be.vdab.groenetenen.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@Entity
@Table(name = "werknemers")
@NamedEntityGraph(name = "Werknemer.metFiliaal", attributeNodes = @NamedAttributeNode("filiaal"))
public class Werknemer implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank
	@SafeHtml
	private String voornaam;
	@NotBlank
	@SafeHtml
	private String familienaam;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "filiaalId")
	@NotNull
	private Filiaal filiaal;
	@NotNull
	@Min(0)
	@NumberFormat(style = Style.NUMBER)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal wedde;
	private long rijksregisterNr;
	
	public long getId() {
		return id;
	}
	public String getVoornaam() {
		return voornaam;
	}
	public String getFamilienaam() {
		return familienaam;
	}
	public Filiaal getFiliaal() {
		return filiaal;
	}
	public BigDecimal getWedde() {
		return wedde;
	}
	public long getRijksregisterNr() {
		return rijksregisterNr;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (rijksregisterNr ^ (rijksregisterNr >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Werknemer other = (Werknemer) obj;
		if (rijksregisterNr != other.rijksregisterNr)
			return false;
		return true;
	}
	
}
