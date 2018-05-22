package be.vdab.groenetenen.messaging;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
class MessagingConfiguratie {
	
	/*
	 * De class Jaxb2Marshaller converteert Java objecten naar XML en converteert XML naar Java objecten. De class gebruikt daartoe intern JAXB.
	*/
	@Bean
	Jaxb2Marshaller marshaller() {
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(OfferteEnOffertesURL.class);	/*Je geeft de classes aan waarvan Jaxb2Marshaller objecten moet converteren van en naar XML.*/
		return marshaller;
		
	}

	/*
	 * Spring gebruikt een bean van de class MarshallingMessageConverter om een Java object, dat je naar een message broker stuurt,
	 * te converteren naar een bepaald data formaat.
	 * 
	 * Je ziet hier voor de eerste keer een method, voorafgegaan door @Bean, die een parameter heeft.
	 * Spring zoekt dan een bean van hetzelfde type en injecteert deze bean in die parameter.
	 * Je hebt daarjuist een bean van het type Jaxb2Marshaller gemaakt.
	 */
	@Bean
	MarshallingMessageConverter converter (Jaxb2Marshaller marshaller) {
		
		return new MarshallingMessageConverter(marshaller, marshaller);
		/*
		 * Je geeft als eerste parameter het object aan dat Spring moet gebruiken om Java objecten te converteren naar XML.
		 * je geeft als tweede parameter het object aan dat Spring moet gebruiken om XML te converteren naar Java objecten.
		 */
	}
	
	// [JMS: Boodschappen ontvangen]
	/*
	 * Als de applicatie een message leest, moet de inhoud van die message terug geconverteerd worden van XML naar een Java object. 
	 * Je maakt daartoe volgende bean definitie.
	 * 
	 * De class DefaultJmsListenerContainerFactory is een toepassing van het factory design pattern.
	 * Spring gebruikt deze class om JmsListener objecten te maken. JmsListener objecten lezen messages uit de message broker.
	 * 
	 * Spring injecteert in de eerste parameter de bean van de class ConnectionFactory. Spring maakt deze bean zelf aan.
	 * De class ConnectionFactory is ook een toepassing van het factory design pattern. 
	 * Spring gebruikt deze class om verbindingen met de message broker te maken.
	 * 
	 * Spring injecteert in de tweede parameter de bean van de class MarshallingMessageConverter. Je hebt deze bean vroeger in dit hfdst gedefinieerd.
	 * Deze bean converteert Java objecten die je in een te versturen message plaatst naar XML en converteert XML in een te lezen message naar Java objecten.
	 */
	@Bean
	DefaultJmsListenerContainerFactory factory(ConnectionFactory connectionFactory, MarshallingMessageConverter converter) {
		
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(converter);
		return factory;
		
	}
}
