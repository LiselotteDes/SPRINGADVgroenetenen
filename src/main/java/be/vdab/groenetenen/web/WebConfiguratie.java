package be.vdab.groenetenen.web;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@Configuration		/*Je tikt @Configuration voor een class waarbinnen je met @Bean Spring beans maakt.*/
class WebConfiguratie {
	
	/*
	 * Je gebruikt voor het eerst @Bean om een Spring bean te maken.
	 * Je schrijft @Bean voorr een method.
	 * Spring maakt, bij het starten van de website van de returnwaarde van die method (hier een FixedLocaleResolver object) één Spring bean.
	 * Spring houdt die bean bij zolang de website draait.
	 */
	@Bean
	FixedLocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("fr", "BE"));		/*De class Locale stelt een combinatie van een land en een taal voor.*/
	}
	
}
