package be.vdab.groenetenen.web;

import javax.validation.constraints.NotNull;

import be.vdab.groenetenen.constraints.Postcode;
import be.vdab.groenetenen.constraints.VanTotPostcodeFormVanKleinerDanOfGelijkAanTot;

@VanTotPostcodeFormVanKleinerDanOfGelijkAanTot
public class VanTotPostcodeForm {
	@NotNull
	@Postcode
	private Integer van;
	@NotNull
	@Postcode
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
