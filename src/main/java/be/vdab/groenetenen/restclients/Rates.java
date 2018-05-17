package be.vdab.groenetenen.restclients;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

class Rates {
	
	/*¨
	 * Standaard denkt Spring dat bij de variabele USD een JSON attribuut uSD hoort.
	 * Je geeft hier aan dat bij de variabele een JSON attribuut USD hoort.
	 */
	@JsonProperty("USD")
	private BigDecimal USD;

	public BigDecimal getUSD() {
		return USD;
	}

	public void setUSD(BigDecimal uSD) {
		USD = uSD;
	}
}
