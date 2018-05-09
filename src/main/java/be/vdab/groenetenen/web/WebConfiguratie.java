package be.vdab.groenetenen.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
//import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration		/*Je tikt @Configuration voor een class waarbinnen je met @Bean Spring beans maakt.*/
class WebConfiguratie implements WebMvcConfigurer {
/*
 * Je moet elke interceptor registreren. Je doet dit in de huidige class.
 * Deze class moet daartoe de interfafe WebMvcConfigurer implementeren.
 */
	
	/*
	 * Je gebruikt voor het eerst @Bean om een Spring bean te maken.
	 * Je schrijft @Bean voorr een method.
	 * Spring maakt, bij het starten van de website van de returnwaarde van die method (hier een FixedLocaleResolver object) één Spring bean.
	 * Spring houdt die bean bij zolang de website draait.
	 */
	@Bean
	/*FixedLocaleResolver*/ 
	SessionLocaleResolver localeResolver() {
		/*return new FixedLocaleResolver(new Locale("fr", "BE"));		/*De class Locale stelt een combinatie van een land en een taal voor.*/
		return new SessionLocaleResolver();
	}
	
	/*
	 * Je doet de registratie van de interceptors zelf in de method addInterceptors, die gedeclareerd is in de interface WebMvcConfigurer.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*
		 * LocaleChangeInterceptor is een interceptor vh Spring framework.
		 * Als de request een parameter locale bevat, 
		 * roept de interceptor de method setLocal van de SessionLocaleResolver bean op en geeft die request parameter mee.
		 * Je onthoudt zo de taal en het land dat de gebruiker gekozen heeft als HTTP session variabele.
		 */
		registry.addInterceptor(new LocaleChangeInterceptor());
	}
}
