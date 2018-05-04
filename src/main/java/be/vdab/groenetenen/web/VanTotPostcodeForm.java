package be.vdab.groenetenen.web;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class VanTotPostcodeForm {
	@NotNull
	@Range(min = 1000, max = 9999)
	private Integer van;
	@NotNull
	@Range(min = 1000, max = 9999)
	private Integer tot;
	/*
	 * Reden dat de private variabelen Integer zijn en geen int.
	 * Een int moet een standaardwaarde hebben (kan niet null zijn, geen object).
	 * Dus in een leeg formulier, toont Spring deze standaardwaarde (wisk. 0).
	 * Een Integer is null bij een leeg formulier,
	 * Spring toont dan ook "lege tekstvakken"!
	 */
	
	public Integer getVan() {
		return van;
	}
	public void setVan(Integer van) {
		this.van = van;
	}
	
	public Integer getTot() {
		return tot;
	}
	public void setTot(Integer tot) {
		this.tot = tot;
	}
}
