package be.vdab.groenetenen.web;

import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import be.vdab.groenetenen.entities.Offerte;
import be.vdab.groenetenen.services.OfferteService;

@Controller
@RequestMapping("offertes")
@SessionAttributes("offerte")
/*
 * "Session scope, validation groups": Je tikt @SessionAttributes bij de controller class.
 * Je geeft de naam van het command object mee dat Spring als HttpSession variabele moet bijhouden.
 */
class OfferteController {
	
	private final OfferteService offerteService;
	
	OfferteController(OfferteService offerteService) {
		this.offerteService = offerteService;
	}
	
	private static final String STAP_1_VIEW = "offertes/stap1";
	
	@GetMapping("toevoegen")
	ModelAndView stap1() {
		return new ModelAndView(STAP_1_VIEW).addObject(new Offerte());
	}
	
	private static final String STAP_2_VIEW = "offertes/stap2";
	
	/*
	 * De method stap1NaarStap2 verwerkt een POST request naar /offertes/toevoegen, op voorwaarde dat die een request parameter stap2 bevat.
	 * Die request is een submit van stap 1.
	 */
	@PostMapping(value="toevoegen", params="stap2")
	String stap1NaarStap2(@Validated(Offerte.Stap1.class) Offerte offerte, BindingResult bindingResult) {
		/*
		 * Je valideert de Offerte attributen die de gebruiker bij de eerste stap intikte met @Validated.
		 * Je geeft als parameter de interface mee, waarmee je de validaties van de eerste stap groepeerde in een validation group.
		 * 
		 * Als stap 1 validatiefouten bevatte, toon je terug stap 1. Anders toon je stap 2.
		 */
		return bindingResult.hasErrors() ? STAP_1_VIEW : STAP_2_VIEW;
	}
	
	/*
	 * De method stap2NaarStap1 verwerkt een POST request naar /offertes/toevoegen, op voorwaarde dat die een request parameter stap1 bevat.
	 * Die request is een klik op de knop Stap 1.
	 */
	@PostMapping(value="toevoegen", params="stap1")
	String stap2NaarStap1(Offerte offerte) {	/*Als de gebruiker op deze knop klikt, hoe je niets te valideren*/
		return STAP_1_VIEW;
	}
	
	private static final String REDIRECT_URL_NA_TOEVOEGEN = "redirect:/";
	@PostMapping(value="toevoegen", params="opslaan")
	String create(@Validated(Offerte.Stap2.class) Offerte offerte, 
				BindingResult bindingResult, 
				SessionStatus sessionStatus) {	
				/*Om de HttpSession variabele te verwijderen geef je aan een @GetMapping of @PostMapping method een SessionStatus parameter mee.*/
		if (bindingResult.hasErrors()) {
			return STAP_2_VIEW;
		}
		offerteService.create(offerte);
		sessionStatus.setComplete();		/*Je verwijdert de HttpSession variabele met de SessionStatus method setComplete.*/
		return REDIRECT_URL_NA_TOEVOEGEN;
	}
}
