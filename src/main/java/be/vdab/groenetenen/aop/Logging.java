package be.vdab.groenetenen.aop;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/* "Foutlogging" als voorbeeld van After Throwing advice. */

@Aspect
@Component
class Logging {

	private static final Logger LOGGER = LoggerFactory.getLogger(Logging.class);
	
	/* Je definieert een method als after throwing advice met @AfterThrowing.
	 * Je geeft een parameter pointcut mee met een pointcut expressie. De expressie hier duidt methods uit be.vdab.groenetenen.web aan.
	 * Als zo'n method een exception werpt, voert Spring na die method de method schrijfException uit.
	 * Je geeft een parameter throwing mee als je de exception van het join point wil weten.
	 * Je tikt daarbij de naam van een Throwable paramerter in de method schrijfException. Spring vult die parameter met de exception van het join point.
	 */
	@AfterThrowing(pointcut = "execution(* be.vdab.groenetenen.web.*.*(..))", throwing = "ex")
	
	/* Je geeft de method een extra Throwable parameter. Spring vult die parameter in met de exception van het join point. */
	void schrijfException(JoinPoint joinPoint, Throwable ex) {
		
		StringBuilder builder = new StringBuilder("Tijdstip\t").append(LocalDateTime.now());
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			builder.append("\nGebruiker\t").append(authentication.getName());
		}
		builder.append("\nMethod\t\t").append(joinPoint.getSignature().toLongString());
		Arrays.stream(joinPoint.getArgs()).forEach(object -> builder.append("\nParameter\t").append(object));
		
		LOGGER.error(builder.toString(), ex);
		
	}
	
}
