package be.vdab.groenetenen.restclients;

import java.math.BigDecimal;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import be.vdab.groenetenen.exceptions.KanKoersNietLezenException;

@Component
class FixerKoersClient implements KoersClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FixerKoersClient.class);
	private final URI fixerURL;		// ! verschil met SpringFund: URI ipv URL
	private final RestTemplate restTemplate;
	
	/*
	 * Spring bevat een bean van het type RestTemplateBuilder.
	 * Deze class is een toepassing van het factory design pattern: ze dient om een RestTemplate object te helpen maken.
	 */
	FixerKoersClient(@Value("${fixerKoersURL}") URI fixerURL, RestTemplateBuilder restTemplateBuilder) {
		this.fixerURL = fixerURL;
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public BigDecimal getDollarKoers() {
		try {
			/*
			 * Je verstuurt een GET request naar de URL die je als parameter meegeeft.
			 * Je geeft als tweede parameter een class mee.
			 * RestTemplate converteert de XML (of JSON) van de response body naar een object van deze class en geeft je dit object als returnwaarde.
			 */
			USDRate rate = restTemplate.getForObject(fixerURL, USDRate.class);
			return rate.getRates().getUSD();
		} catch (RestClientException ex) {
			LOGGER.error("kan koers niet lezen", ex);
			throw new KanKoersNietLezenException();
		}
	}

}
