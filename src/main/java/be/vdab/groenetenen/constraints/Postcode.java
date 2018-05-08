package be.vdab.groenetenen.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/*
 * STAP 1: Je definieert hoe lang Java de annotation behoudt. 
 * RUNTIME betekent dat de annotation bij het uitvoeren van het programma nog ter beschikking is.
 * Als je SOURCE zou kiezen in plaats van RUNTIME, neemt de compiler de annotation niet op in de bytecode.
 */
@Retention(RUNTIME)
/*
 * STAP 2: Je definieert de source onderdelen waarvoor je de annotation kan tikken.
 * a. METHOD voor een method. (Je kan de validation annotation schrijven voor een getter)
 * b. FIELD voor de declaratie van een instance variabele. (Je kan de validation annotation schrijven voor een instance variabele)
 * c. ANNOTATION_TYPE voor de definitie van een andere annotation. (Je kan de annotation gebruiken als basis van nog een andere eigen annotation)
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
/*
 * STAP 3: Je tikt bij een annotation @Constraint als die annotation een 'validation' annotation is.
 * De parameter validatedBy is verplictht te vermelden. 
 * 1°) Je geeft een lege array mee als je de eerste manier gebruikt om een validation annotation te maken.
 * 2°) Je definieert in de huidige source enkel de annotation @Postcode. 
 * Je definieert in een aparte class (PostcodeValidator) de validatie code (ligt de waarde tussen 1000 en 9999?).
 * Je koppelt deze class aan de annotation via @Constraint.
 */
@Constraint(validatedBy = PostcodeValidator.class)

/*
 * (Validation annotation maken als samenstelling van andere validation annotations:
 * STAP 4: Je vermeldt de bestaande bean validation annotation waarop je de eigen bean validation baseert.
 * Je mag ook meer dan één bestaande bean validation annotation vermelden.)
 */
/*@Range(min = 1000, max = 9999)*/

/*
 * STAP 5: Je definieert een annotation met het keyword @interface.
 */
public @interface Postcode {
	/*
	 * (Je geeft aan dat je de message parameter van @Range wil overschrijven met een eigen waarde.
	 * Je doet dit op de volgende regel.)
	 */
	/*@OverridesAttribute(constraint = Range.class, name = "message")*/
	
	/*
	 * Je definieert een annotation parameter. Je doet dit met de syntax van een method declaratie.
	 * String message() betekent dat je aan @Postcode een parameter message kan meegeven.
	 * Die parameter bevat de key van de boodschap die bij de validation annotation hoort.
	 * message is een optionele parameter. Als je hem niet meegeeft, krijgt bij een default waarde.
	 * Je geeft deze waarde mee met het keyword default.
	 */
	String message() default "{be.vdab.groenetenen.constraints.Postcode.message}";
	/*
	 * Elke validation annotation moet ook een optionele parameter groups hebben.
	 * Je ziet verder in de cursus wat groups zijn.
	 */
	Class<?>[] groups() default {};
	/*
	 * Elke validation annotation moet ook een optionele parameter payload hebben.
	 * Het gebruik van deze parameter valt buiten het bereik van deze cursus.
	 */
	Class<? extends Payload>[] payload() default {};
}
