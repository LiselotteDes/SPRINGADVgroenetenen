
/*
 * Je zoekt het HTML element met het attribuut id gelijk aan zoekForm. Dit is het <form> element in de pagina.
 * Je geeft aan dat, bij het submitten (onsubmit) van die form, de browser de JavaScript functie zoekFiliaal moet uitvoeren.
 */
document.getElementById('zoekForm').onsubmit = zoekFiliaal;

function zoekFiliaal() {
	/*
	 * Je maakt een object van het type XMLHttpRequest. Je verstuurt met zo'n object HTTP requests in JavaScript.
	 * Je onthoudt dit object in de variabele request.
	 * Je maakt die variabele met const. Je moet een variabele in JavaScript geen type geven.
	 * Je geeft met const aan dat je de variabele na zijn initialisatie geen andere inhoud geeft.
	 */
	const request = new XMLHttpRequest();
	
	/* Je geeft aan dat je een GET request wil sturen naar de URL van het filiaal waarvan de gebruiker de id intikte.
	 * Je bouwt die URL op als de huidige URL (/filialen/perid), waarin perid vervangen wordt door de inhoud (value) van de textbox met de id filiaalId.
	 * Op dit moment verstuur je de request nog niet.
	 * Als je true meegeeft als 3° parameter zal de browser de request versturen in een achtergrondtaak en de response verwerken in dezelfde achtergrondtaak.
	 * Op die manier blokkeert de browser niet gedurende het verwerken van de request.
	 * Zo kan de gebruiker ondertussen andere handelingen doen in de browser.*/
	request.open("GET", document.getElementById('filiaalId').value, true);
	/* Je plaatst de request header accept op application/json.
	 * Je vraagt zo dat de data in de response body zal uitgedrukt zijn in JSON. JavaScript kan makkelijk JSON data verwerken. */
	request.setRequestHeader('accept', 'application/json');
	/* Je geeft aan dat, als de response binnengekomen is (onload), de browser de JavaScript functie responseIsBinnengekomen moet uitvoeren. */
	request.onload = responseIsBinnengekomen;
	/* Je verstuurt hier pas de request. Zo'n request heeft een AJAX request.*/
	request.send();
	/* Je verhindert met return false dat de form gesubmit wordt. */
	return false;
}

function responseIsBinnengekomen() {
	
	switch (this.status) {				/* Je controleert de response status*/
	case 200:							/* Als die gelijk is aan 200 (OK), heeft de webserver de request correct verwerkt.*/
		/* Je vindt de response body in responseText.
		 * Je converteert met de method parse van het type JSON de JSON data in die response body naar een JavaScript object. */
		const filiaalResource = JSON.parse(this.responseText);
		/* Je zoekt in dit JavaScript object de eigenschap filiaal.
		 * Deze eigenschap is zelf ook een JavaScript object met filiaal eigenschappen (id, naam, ...) */
		const filiaal = filiaalResource.filiaal;
		/* Je zoekt in de pagina het element met de id naam. Dit is het eerste <dd> element in de pagina.
		 * innerHTML stelt de tekst voor tussen <dd ...> en </dd>. Je vervangt deze tekst door de naam van het filiaal. */
		document.getElementById('naam').innerHTML = filiaal.naam;
		const adres = filiaal.adres;
		document.getElementById('adres').innerHTML = adres.straat + ' ' + adres.huisNr + ' ' + adres.postcode + ' ' + adres.gemeente;
		break;
	case 404:							/* De response status is 404 (Not Found). */
		alert('Filiaal bestaat niet');	/* Je toont een popup venster met een foutmelding. */
		break;
	default:							/* De response status verschilt van 200 en van 404. */
		alert('Technisch probleem');
	}
}