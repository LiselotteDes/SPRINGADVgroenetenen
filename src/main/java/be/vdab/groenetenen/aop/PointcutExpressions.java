package be.vdab.groenetenen.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect		/* Je tikt @Aspect vóór de class die de gecentraliseerde pointcuts bevatten. */
class PointcutExpressions {

	/* Je definieert een gecentraliseerde pointcut expressie met @Pointcut voor een method. */
	@Pointcut("execution(* be.vdab.groenetenen.services.*.*(..))")
	
	/* De methodnaam na @Pointcut is de naam die je associeert met de pointcut expressie.
	 * Je associeert be.vdab.aop.PointcutExpressions.services() met de pointcut expressie execution(* be.vdab.groenetenen.services.*.*(..)).
	 * De method heeft geen parameters en geeft void terug.*/
	void services() {}
	
}
