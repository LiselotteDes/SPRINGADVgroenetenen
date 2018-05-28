package be.vdab.groenetenen.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/* "Performance" als voorbeeld van Around advice: je houdt per Service class method bij hoelang het uitvoeren duurde. */

@Aspect
@Component
@Order(1)
class Performance {

	private static final Logger LOGGER = LoggerFactory.getLogger(Performance.class);
	
	/* Je definieert een method als around advice met @Around met een pointcut expressie.
	 * Elke keer je een method oproept die bij deze pointcut expressie hoort, voert Spring de method schrijfPerformance uit, niet de opgeroepen method zelf.
	 */
	@Around("be.vdab.groenetenen.aop.PointcutExpressions.services()")
	
	/* 1°) Je geeft de method die het advice voorstelt een ProceedingJoinPoint parameter van het type ProceedingJoinPoint.
	 * 	   ProceedingJoinPoint erft van JoinPoint en bevat extra functionaliteit die je nodig hebt in around advice.
	 *	   Een around advice method moet als returntype Object hebben.
	 * 2°) De method werpt alle fouten, die het join point eventueel werpt, verder naar de code die oorspronkelijk het join point opgeroepen heeft.
	 */
	Object schrijfPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
		
		/* Voor je het join point uitvoert, onthoud je het resultaat van System.nanoTime(). Dit geeft je een getal dat daarna per nanoseconde met één verhoogt. */
		long voor = System.nanoTime();
		
		try {
			
			/* Bij around advice is het jouw keuze om het join point al of niet uit te voeren. 
			 * Je voert het join point zelf uit met de ProceedingJoinPoint method proceed.
			 * Je doet deze oproep in een try blok gezien het join point een fout kan werpen.
			 * De returnwaarde van de proceed method bevat de returnwaarde van het join point zelf. 
			 * Je geeft die door aan de code die het join point oorspronkelijk heeft opgeroepen. */
			return joinPoint.proceed();
			
		} finally {		// Het finaly blok wordt altijd opgeroepen (ook bij de return van het try blok).
			
			long duurtijd = System.nanoTime() - voor;	// Je berekent de duurtijd van het uitvoeren van het join point.
			LOGGER.info("{} duurde {} nanoseconden", joinPoint.getSignature().toLongString(), duurtijd);
			
		}
		
	}
}
