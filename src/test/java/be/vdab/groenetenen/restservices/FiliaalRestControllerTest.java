package be.vdab.groenetenen.restservices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import be.vdab.groenetenen.entities.Filiaal;
import be.vdab.groenetenen.services.FiliaalService;
import be.vdab.groenetenen.valueobjects.Adres;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
/*@AutoConfigureTestDatabase voert zoals @DataJpaTest de test uit met een H2 (in memory) database.
  In tegenstelling tot @DataJpaTest laadt @AutoConfigureTestDatabse alle beans, niet enkel de repository beans.*/
public class FiliaalRestControllerTest {
	
	// Je injecteert de IOC container, die de Spring beans bevat, als een WebApplicationContext.
	@Autowired
	private WebApplicationContext context;
	// Je injecteert de bean die de interface FiliaalService implementeert.
	@Autowired
	private FiliaalService filiaalService;
	
	private Filiaal filiaal;
	// Je kan met een MockMvc object HTTP requests versturen in de integration test.
	private MockMvc mvc;
	
	@Before
	public void before() {
		filiaal = new Filiaal();
		filiaal.setNaam("test");
		filiaal.setWaardeGebouw(BigDecimal.TEN);
		filiaal.setInGebruikName(LocalDate.now());
		filiaal.setAdres(new Adres("straat", "huisNr", 1000, "gemeente"));
		
		/*Je voegt in elke test een filiaal toe aan de database.
		 *Gezien Spring op het einde van de test een rollback doet, blijft dit filiaal maar in de database gedurende die test. */
		filiaalService.create(filiaal);
		
		/*Je verbindt de MockMVC variabele die je gemaakt hebt met de WebApplicationContext die je geïnjecteerd hebt.
		 *De HTTP requests die je straks via de MockMVC verstuurt, stuurt Spring dan naar de REST controllers in die WebApplicationContext.*/
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void filiaalLezenDatNietBestaat() throws Exception {
		/* (UITGESCHREVEN)
		ResultMatcher notFound = MockMvcResultMatchers.status()
													  .isNotFound();
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/filialen/-666")
																	  .accept(MediaType.APPLICATION_XML);
		this.mvc.perform(builder)
			    .andExpect(notFound);
		*/
		
		/* (KORT) */
		/*
		 * Je stuurt een GET request naar de URL van een niet-bestaand filiaal.
		 * De method get is een static method van de class MockMvcRequestBuilders. Je importeerde deze method boven in de source met static import.
		 */
		mvc.perform(get("/filialen/-666")
					/*Je plaatst de request header Accept op application/xml.*/
					.accept(MediaType.APPLICATION_XML))
			/*
			 * Je controleert of de response status code Not Found (404) is.
			 * De method status is een static method van de class MockMvcResultMatchers. Je importeerde deze method boven in de source met static import.
			 */
		   .andExpect(status().isNotFound());
	}
	
	@Test
	public void filiaalDatBestaatLezenInXMLFormaat() throws Exception {
			/*Je stuurt een GET request naar de URL van het filiaal dat je aan de database toevoegde.*/
		mvc.perform(get("/filialen/" + filiaal.getId()).accept(MediaType.APPLICATION_XML))
			/*Je controleert of de response status code OK (200) is.*/
			.andExpect(status().isOk())
			/*Je controleert of de response hader content-type gelijk is aan application/xml.*/
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
			/*
			 * Je zoekt met XPath in de XML data in de response body in het element filiaalResource
			 * het child element filiaal en daarin het child element id.
			 * De method xpath is een static method van de class MockMvcResultMatchers. Je importeerde deze method boven in de source met static import.
			 */
			.andExpect(xpath("/filiaalResource/filiaal/id")
					/*Je controleert of de inhoud van dit element id gelijk is aan de id van het filiaal dat je aan de database toevoegde.*/
					.string(String.valueOf(filiaal.getId())));
	}
	
	@Test
	public void filiaalLezenDatBestaatInJSON() throws Exception {
		mvc.perform(get("/filialen/" + filiaal.getId()).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			/*
			 * Je zoekt met JSONPath in de rexponse body in het JSON object dat de volledige response body vult (voorgesteld door $)
			 * de eigenschap filiaal en daarin de eigenschap id.
			 * De method jsonPath is een static method van de class MockMvcResultMatchers. Je importeerde deze method boven id source met static import.
			 * Je controleert of de inhoud van deze property id gelijk is aan de id van het filiaal dat je aan de database toevoegde.
			 */
			.andExpect(jsonPath("$.filiaal.id").value((int) filiaal.getId()));
	}
	
	@Test
	public void correctFiliaalToevoegen() throws Exception {
		/*Je vult een String met de inhoud van het bestand nieuwCorrectFiliaal.xml.
		 *De method copyToString sluit zelf de Stream die je als parameter aanbiedt.*/
		String nieuwCorrectFiliaal = FileCopyUtils.copyToString(
				new InputStreamReader(context.getResource("classpath:nieuwCorrectFiliaal.xml").getInputStream()));
		
		mvc.perform(post("/filialen")					/*Je doet een POST request naar /filialen*/
				.contentType(MediaType.APPLICATION_XML)	/*Je plaatst de request header content-type op application/xml.*/
				.content(nieuwCorrectFiliaal))			/*Je vult de request body met de inhoud van het bestand nieuwCorrectFiliaal.xml.*/
			.andExpect(status().isCreated());			/*Je controleert of de response status code Created (201) is.*/
	}
	
	@Test
	public void filiaalMetFoutToevoegen() throws Exception {
		String nieuwFiliaalMetFout = FileCopyUtils.copyToString(new InputStreamReader(
				context.getResource("classpath:nieuwFiliaalMetFout.xml").getInputStream()));
		
		mvc.perform(post("/filialen")
				.contentType(MediaType.APPLICATION_XML)
				.content(nieuwFiliaalMetFout))
			.andExpect(status().isBadRequest());		/*Je controleert of de response status code Bad request (400) is.*/
	}
}
