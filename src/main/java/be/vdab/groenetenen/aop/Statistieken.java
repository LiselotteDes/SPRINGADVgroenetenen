package be.vdab.groenetenen.aop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* "Statistieken" als After Finally advice voorbeeld: Je houdt per Service class method bij hoeveel keer die method opgeroepen werd. */

@Aspect
@Component
class Statistieken {

	private static final Logger LOGGER = LoggerFactory.getLogger(Statistieken.class);
	/* De key van deze map is een String met de signatuur van een joint point. De value is hoeveel keer dit join point werd opgeroepen. */
	private final ConcurrentHashMap<String, AtomicInteger> statistieken = new ConcurrentHashMap<>();
	
	/* Je definieert een method als after finally advice met @After met een pointcut expressie.
	 * Elke keer je een method oproept die bij deze pointcut expressie hoort, voert Spring na de opgeroepen method de method statistiekenBijwerken uit,
	 * ongeacht of de opgeroepen method al of niet een exception werpt.
	 */
	@After("execution(* be.vdab.groenetenen.services.*.*(..))")
	void statistiekBijwerken(JoinPoint joinPoint) {
		
		String joinPointSignatuur = joinPoint.getSignature().toLongString();
		
		/* Als de join point signatuur nog niet voorkomt in de Map, voe je een entry toe met als key de joint point signature en als value 1.
		 * De ConcurrentHashMap method putIfAbsent retourneert de vorige value geassocieerd met de gespecifieerde key, of null als er geen mapping was voor de key.*/
		AtomicInteger vorigAantalOproepen = statistieken.putIfAbsent(joinPointSignatuur, new AtomicInteger(1));
		
		/* Als de join point signatuur al voorkwam in de Map, verhoog je de bijheborende value. */
		int aantalOproepen = vorigAantalOproepen == null ? 1 : vorigAantalOproepen.incrementAndGet();
		
		LOGGER.info("{} werd {} keer opgeroepen", joinPointSignatuur, aantalOproepen);
		/* Spring vervangt de eerste {} door de inhoud van de 2° parameter (joinPointSignatuur).
		 * Spring vervangt de tweede {} door de inhoud van de 3° parameter (aantalOproepen).*/
	}
}
