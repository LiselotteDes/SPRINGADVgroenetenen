package be.vdab.groenetenen.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.vdab.groenetenen.entities.Filiaal;
import be.vdab.groenetenen.services.FiliaalService;

@Controller
@RequestMapping(path = "filialen", produces = MediaType.TEXT_HTML_VALUE)
/*
 * "Niet-browser clients rest requests"
 * @RequestMapping: produces bevat het data formaat van de response. De MediaType constante TEXT_HTML_VALUE bevat text/html.
 * Je geeft dus aan dat de response data in HTML formaat bevat.
 * Spring stuurt vanaf nu requests waarvan de URL begint met /filialen naar de controller, als de request header Accept de waarde text/html bevat.
 * Dit is het geval bij browsers, niet bij andere clients.
 */
class FiliaalController {
	private final static String VAN_TOT_POSTCODE_VIEW = "filialen/vantotpostcode";
	private final FiliaalService filiaalService;
	
	FiliaalController(FiliaalService filiaalService) {
		this.filiaalService = filiaalService;
	}
	
	@GetMapping("vantotpostcode")
	ModelAndView vanTotPostcode() {
		return new ModelAndView(VAN_TOT_POSTCODE_VIEW).addObject(new VanTotPostcodeForm());
	}
	
	@GetMapping(params = { "van", "tot" })
	ModelAndView vanTotPostcode(@Valid VanTotPostcodeForm form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ModelAndView(VAN_TOT_POSTCODE_VIEW);
		}
		return new ModelAndView(VAN_TOT_POSTCODE_VIEW).addObject("filialen", filiaalService.findByPostcode(form.getVan(), form.getTot()));
	}
	
	// "Automatische String naar Entity converters"
	private static final String FILIAAL_VIEW = "filialen/filiaal";
	private static final String REDIRECT_FILIAAL_NIET_GEVONDEN = "redirect:/";
	/*
	 * Je geeft aan dat de method read requests verwerkt met een URL vermeld bij @RequestMapping, 
	 * gevolgd door een path variabele die je hier aanduidt met de naam id.
	 */
	@GetMapping("{id}")
	ModelAndView read(@PathVariable("id") Optional<Filiaal> filiaal, RedirectAttributes redirectAttributes) {
		/*
		 * Je geeft hierboven aan dat Spring de parameter filiaal moet invullen met de inhoud van de path variabele id.
		 * Spring gebruikt op dit moment de converter van Spring data die de path variabele omzet in een Optional<Filiaal> object.
		 */
		if (filiaal.isPresent()) {
			return new ModelAndView(FILIAAL_VIEW).addObject(filiaal.get());
		}
		redirectAttributes.addAttribute("fout", "Filiaal niet gevonden");
		return new ModelAndView(REDIRECT_FILIAAL_NIET_GEVONDEN);
	}
	
	/* "Javascript code als REST client (AJAX requests)" */
	
	private static final String PER_ID_VIEW = "filialen/perid";
	
	@GetMapping("perid")
	String findById() {
		return PER_ID_VIEW;
	}
}
