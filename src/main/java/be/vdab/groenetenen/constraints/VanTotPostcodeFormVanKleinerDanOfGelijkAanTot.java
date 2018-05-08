package be.vdab.groenetenen.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
/*
 * Je definieert de source onderdelen waarvoor je de annotation kan tikken.
 * a. TYPE voor een class.
 * b. ANNOTATION_TYPE voor de definitie van een andere annotation.
 *    (Je kan de annotation gebruiken als basis van nog een andere eigen annotation.)
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Constraint(validatedBy = VanTotPostcodeFormVanKleinerDanOfGelijkAanTotValidator.class)
public @interface VanTotPostcodeFormVanKleinerDanOfGelijkAanTot {
	String message() default "{be.vdab.groenetenen.constraints.VanKleinerDanOfGelijkAanTot.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
