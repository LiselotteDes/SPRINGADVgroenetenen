package be.vdab.groenetenen.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import be.vdab.groenetenen.entities.Filiaal;

/*
 * Je geeft, bij het erven van de generic interface JpaRepository, tussen < en > het type entity mee dat bij je interface hoort,
 * daarna het variabele type dat bij de primary key hoort.
 */
public interface FiliaalRepository extends JpaRepository<Filiaal,Long> {
	
	List<Filiaal> findByAdresPostcodeBetweenOrderByAdresPostcode(int van, int tot);
	/*
	 * Spring maakt automatisch een JPQL query gebaseerd op een Java method als je:
	 * - De method naam begint met findBy.
	 * - Daarna een eigenschap van de huidige (Filiaal) entity: adres.postcode. Spring gebruikt dit in het where deel van de query.
	 * - Daarna een operator voor het where deel: Between.
	 * - Daarna een sorteeropdracht: OrderBy
	 * - Daarna de eigenschap waarop je wil sorteren: adres.postcode.
	 * - Spring gebruikt de method parameters als query parameters in het where deel.
	 * => De gegenereerde JPQL query is dus:
	 * 	  select f from Filiaal f where f.adres.postcode between :van and :tot order by f.adres.postcode
	 */
}
