package be.vdab.groenetenen.web;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
class IndexController {
	private static final String VIEW = "index";
	
	private String begroeting() {
		int uur = LocalDateTime.now().getHour();
		if (uur >= 6 && uur < 12) {
			return "goedeMorgen";	/*Je geeft geen hard gecodeerde Nederlandstalige tekst meer terug, maar een key uit de resource bundles.*/
		}
		if (uur >= 12 && uur < 18) {
			return "goedeMiddag";
		}
		return "goedeAvond"; 
	}
	
	@GetMapping
	ModelAndView index() {
		return new ModelAndView(VIEW, "begroeting", begroeting());
		/*
		 * Je geeft de request door aan de Thymeleaf pagina index.html.
		 * Je moet daarbij de extensie html niet vermelden.
		 */
	}
}
