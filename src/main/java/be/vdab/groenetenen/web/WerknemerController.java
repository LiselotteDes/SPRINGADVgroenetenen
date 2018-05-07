package be.vdab.groenetenen.web;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import be.vdab.groenetenen.services.WerknemerService;

@Controller
@RequestMapping("werknemers")
class WerknemerController {
	
	private final WerknemerService werknemerService;
	
	WerknemerController(WerknemerService werknemerService) {
		this.werknemerService = werknemerService;
	}
	
	private static final String WERKNEMERS_VIEW = "werknemers/werknemers";
	
	@GetMapping
	/*ModelAndView lijst() {
		return new ModelAndView(WERKNEMERS_VIEW, "werknemers", werknemerService.findAll());
	}	Wijzig bij "Pagineren" */
	ModelAndView lijst(Pageable pageable) {
	/*
	 * De JpaRepository method findAll die één pagina records teruggeeft, heeft de parameter Pageable nodig,
	 * waarin je informatie meegeeft over de gewenste pagina.
	 * Je moet zelf geen Pageable object maken: je kan een Pageable parameter toevoegen aan een @GetMapping method van een Controller.
	 * Spring vult de eigenschappen van deze Pageable parameter aan de hand van volgende request parameters: page, size, sort.
	 */
		return new ModelAndView(WERKNEMERS_VIEW, "page", werknemerService.findAll(pageable));
	}
}
