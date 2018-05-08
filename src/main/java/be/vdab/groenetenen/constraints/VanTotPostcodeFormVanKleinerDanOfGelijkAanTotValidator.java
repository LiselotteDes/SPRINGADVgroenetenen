package be.vdab.groenetenen.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import be.vdab.groenetenen.web.VanTotPostcodeForm;

public class VanTotPostcodeFormVanKleinerDanOfGelijkAanTotValidator 
	/*@VanTotPostcodeFormVanKleinerDanOfGelijkAanTot mag voorkomen bij de class VanTotPostcodeForm.*/
	implements ConstraintValidator<VanTotPostcodeFormVanKleinerDanOfGelijkAanTot, VanTotPostcodeForm> {
	
	@Override
	public void initialize(VanTotPostcodeFormVanKleinerDanOfGelijkAanTot arg0) {
	}
	
	/*
	 * Bean validation roept de method isValid op bij het valideren van een VanTotPostcodeForm object.
	 * Je geeft true terug bij een correcte validatie.
	 */
	@Override
	public boolean isValid(VanTotPostcodeForm form, ConstraintValidatorContext context) {
		/*
		 * Je produceert geen foutmelding als de te valideren waarden gelijk zijn aan null, 
		 * zoals de ingebakken annotations (@Min, @Max, ...) dat ook doen.
		 */
		if (form.getVan() == null || form.getTot() == null) {
			return true;
		}
		/*De validatie is correct als van kleiner is of gelijk aan tot.*/
		return form.getVan() <= form.getTot();
	}
}
