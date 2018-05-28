package be.vdab.groenetenen.aop;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/* "Auditing" als voorbeeld van "Before/After Returning advice". Je houdt met auditing bij welke gebruiker op welk moment welke use case uitvoert. */

@Aspect			// Je definieert een class als aspect (= de combinatie van advice en pointcut) met @Aspect.
@Component		// Een aspect moet zelf ook een Spring bean zijn, vandaar @Component.
class Auditing {

	private static final Logger LOGGER = LoggerFactory.getLogger(Auditing.class);
	
	/* Je definieert een method als before advice met @Before met een pointcut expressie.
	 * De expressie hier duidt methods uit be.vdab.groenetenen.services aan.
	 * Als je zo'n method oproept, voert Spring eerst de method schrijfAudit (zie hieronder) uit, daarna de opgeroepen method zelf.
	 */
	// @Before("execution(* be.vdab.groenetenen.services.*.*(..))")
	
	/* Je definieert een method als after returning advice met @AfterReturning.
	 * Je geeft een parameter pointcut mee met een pointcut expressie. De expressie hier duidt methods uit be.vdab.groenetenen.services aan.
	 * Als je zo'n method oproept, voert Spring na die method de method schrijfAudit (zie hieronder) uit als de opgeroepen method geen exception werpt.
	 * Je geeft een parameter returning mee als je de returnwaarde van het join point wil weten.
	 * Je tikt daarbij de naam van een Object parameter in de method schrijfAudit. Spring vult die parameter met de returnwaarde van het join point.
	 */
	@AfterReturning(pointcut = "be.vdab.groenetenen.aop.PointcutExpressions.services()", returning = "returnValue")
	
	/* Je geeft de method, die het advice voorstelt, een parameter van het type JoinPoint.
	 * Deze parameter verwijst naar het join point waarvóór Spring het advice uitvoert en geeft interessante informatie over dit join point.
	 * Je geeft de method een extra Object parameter mee, als het een AfterReturning method is. Spring vult die parameter in met de returnwaarde v/h join point.
	 */
	void schrijfAudit(JoinPoint joinPoint, Object returnValue) {
		
		/*Je bouwt de auditing informatie op in een StringBuilder object. Je voegt hier het tijdstip toe waarop een service method werd uitgevoerd.*/
		StringBuilder builder = new StringBuilder("Tijdstip\t").append(LocalDateTime.now());
		
		/*Je haalt met SecurityContextHolder.getContext().getAuthentication() een Authentication object op. Dit bevat informatie over de ingelogde gebruiker.*/
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			builder.append("\nGebruiker\t").append(authentication.getName());
		}
		
		/*De method getSignature van een JoinPoint geeft je de mehtod declaratie van het join point als een Signature object.
		 *De method toLongString geeft je de package-, interface- én methodnaam.*/
		builder.append("\nMethod\t\t").append(joinPoint.getSignature().toLongString());
		
		/*getArgs geeft je een Object array met de parameterwaarden van het join point.*/
		Arrays.stream(joinPoint.getArgs()).forEach(object -> builder.append("\nParameter\t").append(object));
		
		/*Extra code in het geval van een AfterReturning method ipv Before*/
		if (returnValue != null) {						// Als het returntype van het join point void is, bevat returnValue null.
			
			builder.append("\nReturn\t\t");
			if (returnValue instanceof Collection) {	// Als de returnwaarde een Collection (List, Set, Map) is, 
				// toon je enkel het aantal elementen in de verzameling, om de omvang van de auditing output te beperken.
				builder.append(((Collection<?>) returnValue).size()).append(" objects");	
			} else {
				builder.append(returnValue.toString());	// Anders toon je de returnwaarde.
			}
			
		}/*Einde extra AfterReturning code*/
		
		LOGGER.info(builder.toString());
		
	}
}
