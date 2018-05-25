package be.vdab.groenetenen.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

@EnableWebSecurity	// @EnableWebSecurity integreert Spring security en Spring MVC
// Je erft van WebSecurityConfigurerAdapter. Je kan in je class methods overriden om Spring security te configureren.
class SecurityConfig extends WebSecurityConfigurerAdapter {	

	private static final String MANAGER = "manager";
	private static final String HELPDESKMEDEWERKER = "helpdeskmedewerker";
	private static final String MAGAZIJNIER = "magazijnier";
	// [Afwijkende tabel structuren]
	private static final String USERS_BY_USERNAME = "select naam as username, paswoord as password, actief as enabled from gebruikers where naam = ?";
	private static final String AUTHORITIES_BY_USERNAME = "select gebruikers.naam as username, rollen.naam as authorities"
														+ " from gebruikers inner join gebruikersrollen"
														+ " on gebruikers.id = gebruikersrollen.gebruikerid"
														+ " inner join rollen"
														+ " on rollen.id = gebruikersrollen.rolid"
														+ " where gebruikers.naam = ?";
	
	/* Je maakt een bean van het type InMemoryUserDetailsManager. Je houdt met deze bean principals bij in het interne geheugen.
	 * Je leest ze verder in de cursus uit een database. */
	/*@Bean
	InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		
		return new InMemoryUserDetailsManager(
						User.builder().username("joe").password("{noop}theboss").authorities(MANAGER).build(),
						User.builder().username("averell").password("{noop}hungry").authorities(HELPDESKMEDEWERKER, MAGAZIJNIER).build());*/
		/*
		 * Je geeft aan de InMemoryUserDetailsManager constructor enkele Users (principals) mee.
		 * Je maakt een User met het builder design pattern. Je vermeldt de gebruikersnaam, het passwoord en de authorities.
		 * Voor het paswoord staat {noop}. Dit betekent dat Spring het paswoord niet-versleuteld onthoudt. Je leest verder in de cursus werken met versleutelde paswoorden.
		 */
		
	/*}*/	// Vervangen in "Principals en authorization in een database"
	
	@Bean
	JdbcDaoImpl jdbcDaoImpl(DataSource dataSource) {
		
		/* De class JdbcDaoImpl leest principals uit de database. Deze class moet weten uit welke database hij de principals moet lezen.
		 * Je injecteert daartoe de DataSource die al naar de database groenetenen verwijst in de huidige method.
		 */
		
		// [Default tabel structuren]
		JdbcDaoImpl impl = new JdbcDaoImpl();
		impl.setDataSource(dataSource);
		
		// [Afwijkende tabel structuren]
		impl.setUsersByUsernameQuery(USERS_BY_USERNAME);
		impl.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME);
		
		return impl;
	}
	
	@Override	// Je overridet de method die de web eigenschappen van Spring security configureert.
	public void configure(WebSecurity web) throws Exception {
		
		/* Spring security moet geen beveiliging doen op URL's die passen bij /images/**. ** betekent dat het patroon ook subfolders van /images bevat. */
		web.ignoring()
			.mvcMatchers("/images/**")
			.mvcMatchers("/css/**")
			.mvcMatchers("/scripts/**");
		
	}
	
	@Override	// Je overridet de method die de HTTP beveiliging van Spring security configureert.
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			//.csrf().disable()	// Zet de CSRF beveiliging die standaard actief is in Spring af.
			.formLogin()			// De gebruiker authenticeert zich door zijn gebruikersnaam en paswoord te tikken in een HTML form.
			.loginPage("/login")	// Je definieert deze URL als inlogpagina
			.and()
			.logout()				// Je activeert de uitlog functionaliteit
			.logoutSuccessUrl("/")	// Spring security toont na het afmelden standaard de inlogpagina. Je wijzigt dit naar de welkompagina.
			.and()
			.authorizeRequests()
			 // Je definieert autorisatie: enkel ingelogde gebruikers met de authority manager kunnen de URL /offertes/toevoegen aanspreken.
			.mvcMatchers("/offertes/toevoegen").hasAuthority(MANAGER)
			 // Enkel ingelogde gebruikers met de authority magazijnier of helpdeskmedewerker kunnen de URL /werknemers aanspreken.
			.mvcMatchers("/werknemers").hasAnyAuthority(MAGAZIJNIER, HELPDESKMEDEWERKER)
			 // Je geeft alle (ook anonieme) gebruikers toegang tot de welkompagina en de loginpagina.
			.mvcMatchers("/", "/login").permitAll()
			 // Voor alle andere URL's moet de gebruiker minstens ingelogd zijn.
			.mvcMatchers("/**").authenticated();
		
	}
	
}
