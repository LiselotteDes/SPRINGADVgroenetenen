package be.vdab.groenetenen.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*
 * Een class, met de validatiecode van een bean validation annotation, implementeert ConstraintValidator.
 * Je geeft tussen < en > eerst de bean validation annotation mee.
 * Je definieert daarna het type variabele waarbij de annotation mag voorkomen. @Postcode mag voorkomen bij een Integer (of int) variabele.
 */
public class PostcodeValidator implements ConstraintValidator<Postcode, Integer> {
	
	private static final int MIN_POSTCODE = 1000;
	private static final int MAX_POSTCODE = 9999;
	
	@Override
	public void initialize(Postcode postcode) {
		/*Het gebruik van de method initialize valt buiten deze cursus.*/
	}
	/*
	 * Bean validation roept de method isValid op bij het valideren van een variabele voorzien van @Postcode.
	 * Je geeft true terug als de variabele een correcte waarde bevat.
	 */
	@Override
	public boolean isValid(Integer postcode, ConstraintValidatorContext context) {
		/*
		 * Zoals ingebakken annotations (@Min, @Max, ...) produceer je geen foutmelding als de te valideren waarde gelijk is aan null:
		 * de validatie hiervan gebeurt door @NotNull.
		 */
		return postcode == null || (postcode >= MIN_POSTCODE && postcode <= MAX_POSTCODE);
	}
}
