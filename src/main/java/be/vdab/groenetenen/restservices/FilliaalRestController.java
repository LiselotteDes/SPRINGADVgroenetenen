package be.vdab.groenetenen.restservices;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import be.vdab.groenetenen.entities.Filiaal;
import be.vdab.groenetenen.exceptions.FiliaalHeeftNogWerknemersException;
import be.vdab.groenetenen.exceptions.FiliaalNietGevondenException;
import be.vdab.groenetenen.services.FiliaalService;

/*
 * Deze controller verwerkt niet-browser requests.
 * - Je gebruikt @RestController bij een controller die niet-browser requests verwerkt.
 * - De URL ("/filialen") is dezelfde als bij FiliaalController, maar zonder produces=MediaType.TEXT_HTML_VALUE.
 *   FiliaalRestController verwerkt daarom requests waarvan de URL begint met /filialen, maar waarvan de request header Accept verschilt van text/html.
 * - Je moet de URL aangeven die een type entity voorstelt (/filialen stelt Filiaal entities voor).
 *   Je tikt @ExposeResourcesFor(Filiaal.class) voor de class.
 *   Spring HATEOAS zoekt de @RequestMapping regel bij de controller class en vindt daarin dat Filiaal entities zich bevinden op de URL /filialen.
 */
@RestController
@RequestMapping("/filialen")
@ExposesResourceFor(Filiaal.class)
class FilliaalRestController {
	
	private final FiliaalService filiaalService;
	/*
	 * Spring maakt een bean met als interface EntityLinks.
	 * Je maakt met deze bean <link ...> elementen in XML.
	 * Je injecteert deze bean in de huidige class FiliaalRestController.
	 */
	private final EntityLinks entityLinks;
	
	FilliaalRestController(FiliaalService filiaalService, EntityLinks entityLinks) {
		this.filiaalService = filiaalService;
		this.entityLinks = entityLinks;
	}
	
	/*
	 * Spring converteert in een @RestController class de returnwaarde van een @GetMapping method naar XML of JSON en vult hiermee de response body.
	 * > Als de request een Accept header bevat gelijk aan application/xml, vult Spring de response met een XML voorstelling van het filiaal.
	 *   Spring plaatst de Content-type response header op application/xml.
	 *   Spring doet dit ook als in de request de Accept header ontbreekt.
	 * > Als de request een Accept header bevat gelijk aan application/json, vult Spring de response met een JSON voorstelling van het filiaal.
	 *   Spring plaatst de Content-type response header op application/json.
	 * Spring data converteert de path variabele met het id van het filiaal object naar een Filiaal object door dit Filiaal object uit de db te lezen.
	 */
	@GetMapping("{filiaal}")
	FiliaalResource read(@PathVariable Optional<Filiaal> filiaal) {
		/* (OUD)
		if (! filiaal.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}*/
		
		/* (BETER) */
		if (filiaal.isPresent()) {
			return new FiliaalResource(filiaal.get(), entityLinks);
		}
		throw new FiliaalNietGevondenException();
	}
	
	/*
	 * @ExceptionHandler betekent dat de method exceptions behandelt die je in @GetMapping methods werpt.
	 * De method behandelt enkel exceptions van het type FiliaalNietGevondenException.
	 * @ResponseStatus bevat de statuscode die je naar de browser wil sturen als een FiliaalNietGevondenException optreedt.
	 */
	@ExceptionHandler(FiliaalNietGevondenException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	void filiaalNietGevonden() {
	}
	
	/*
	 * "DELETE request"
	 * Het returntype van de method is void. Spring maakt dan een response met een lege body. 
	 */
	@DeleteMapping("{filiaal}")
	void delete(@PathVariable Optional<Filiaal> filiaal) {
		/* (OUD (zonder Optional))
		if (filiaal == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		try {
			filiaalService.delete(filiaal.getId());
		} catch (FiliaalHeeftNogWerknemersException ex) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}*/
		
		if (filiaal.isPresent()) {
			filiaalService.delete(filiaal.get().getId());	// Deze method kan een FiliaalHeeftNogWerknemersException werpen.
		}
		throw new FiliaalNietGevondenException(); 
		// De method filiaalNietGevonden vertaalt deze excetpion naar een response met status code 404 (Not Found).
	}
	
	@ExceptionHandler(FiliaalHeeftNogWerknemersException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String filiaalHeeftNogWerknemers() {
		/*
		 * Je vertaalt de FiliaalHeeftNogWerknemersException naar een response met status code 409 (Conflict) 
		 * en een response body "filiaal heeft nog werknemers".
		 */
		return "filiaal heeft nog werknemers";
	}
	
	/*
	 * "POST request"
	 * Annotations:
	 * - @ResponseStatus(HttpStatus.CREATED): 
	 *   Als de method geen exceptions werpt, vult Srping de response status code met de waarde uit @ResponseStatus.
	 *   De constante CREATED bevat de waarde 201 (Created).
	 * Parameters:
	 * - Spring vertaalt met @RequestBody de request body naar een Filiaal object.
	 * - Spring valideert met @Valid dit Filiaal object ten opzichte van de validation annotations.
	 *   Als er validatiefouten zijn, voert Spring de method create niet uit, maar werpt een MethodArgumentNotValidException.
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	HttpHeaders create(@RequestBody @Valid Filiaal filiaal) {
		filiaalService.create(filiaal);		/*Je voegt het filiaal toe aan de database*/
		HttpHeaders headers = new HttpHeaders();
		/*
		 * Je maakt een voorstelling van het XML element
		 * <link rel="self" href="http://localhost:8080/filialen/11"/>
		 * Het href attribuut bevat de URL van het toegevoegde filiaal (als de id 11 is).
		 */
		Link link = entityLinks.linkToSingleResource(Filiaal.class, filiaal.getId());
		// Je maakt met het href attribuut een URI. Je vult de response hader Location met die URI.
		headers.setLocation(URI.create(link.getHref()));
		return headers;
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)	/*Je geeft aan dat de method die hierop volgt een response naar de browser zal sturen als een MANVException optreedt.*/
	@ResponseStatus(HttpStatus.BAD_REQUEST)						/*Je geeft hier de status code van die response aan.*/
	// Er zijn validatiefouten opgetreden in de filiaal data. Spring plaatst die fouten in de MethodArgumentNotValidException parameter.
	String filiaalMetVerkeerdeProperties(MethodArgumentNotValidException ex) {
		StringBuilder fouten = new StringBuilder();
		// getBindingResult().getFieldErrors() geeft een verzameling FieldError objecten: de Filiaal properties met validatiefouten.
		ex.getBindingResult().getFieldErrors()
		  /*
		   * Je voegt per validatiefout aan de response body een regel toe met:
		   * - de naam van de verkeerde propertie (error.getField())
		   * - een : teken
		   * - de foutboodschap (error.getDefaultMessage())
		   * - een enter teken ("\n")
		   */
		  .forEach(error -> fouten.append(error.getField()).append(':')
								  .append(error.getDefaultMessage()).append('\n'));
		// Je verwijdert het overtollige laatste Enter teken na de laatste regel
		fouten.deleteCharAt(fouten.length() - 1);
		return fouten.toString();
	}
	
	// "PUT request"
	@PutMapping("{id}")
	void update(@RequestBody @Valid Filiaal filiaal) {
		filiaalService.update(filiaal);
	}
	
	@GetMapping
	FilialenResource findAll() {
		return new FilialenResource(filiaalService.findAll(), entityLinks);
	}
}
