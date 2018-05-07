package be.vdab.groenetenen.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import be.vdab.groenetenen.entities.Filiaal;
import be.vdab.groenetenen.valueobjects.Adres;

@RunWith(SpringRunner.class)
@DataJpaTest
/*
 * @DataJpaTest doet heel wat werk:
 * a. Hij maakt met H2 een lege, in-memory database.
 * b. Hij maakt lege tabellen in die database. Hij baseert de tabel namen, kolom namen, kolom types, primary keys en foreign keys 
 *    op de entity classen en value object classes en de JPA annotations in die classes.
 * c. Hij maakt een DataSource bean die naar deze database verwijst.
 * d. Hij maakt een EntityManager bean.
 * e. Hij maakt beans van de classes, gegenereerd door Spring Data, die gebaseerd zijn op de interfaces uit je repository layer.
 */
public class FiliaalRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final String FILIALEN = "filialen";
	@Autowired
	private FiliaalRepository repository;

	@Test
	public void create() {
		// je maakt een nieuw Filiaal object
		Adres adres = new Adres("straat", "huisNr", 1000, "gemeente");
		Filiaal filiaal = new Filiaal();
		filiaal.setNaam("test");
		filiaal.setAdres(adres);
		filiaal.setWaardeGebouw(BigDecimal.ZERO);
		filiaal.setInGebruikName(LocalDate.now());
		// aantal filialen in de tabel
		int aantalFilialen = super.countRowsInTable(FILIALEN);
		// sla het gemaakte filiaal nu op in de db
		repository.save(filiaal);
		// *** aantal filialen in tabel is met één vermeerderd
		assertEquals(aantalFilialen + 1, super.countRowsInTable(FILIALEN));
		// *** id van het object toegekend door JPA is niet 0 ***
		assertNotEquals(0, filiaal.getId());
		// *** het record met het id van het object wordt teruggevonden in de db ***
		assertEquals(1, super.countRowsInTableWhere(FILIALEN, "id=" + filiaal.getId()));
	}

}
