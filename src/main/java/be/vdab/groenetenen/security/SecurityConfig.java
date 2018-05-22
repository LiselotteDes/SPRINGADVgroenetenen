package be.vdab.groenetenen.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity	// @EnableWebSecurity integreert Spring security en Spring MVC
// Je erft van WebSecurityConfigurerAdapter. Je kan in je class methods overriden om Spring security te configureren.
class SecurityConfig extends WebSecurityConfigurerAdapter {	

	private static final String MANAGER = "manager";
	private static final String HELPDESKMEDEWERKER = "helpdeskmedewerker";
	private static final String MAGAZIJNIER = "magazijnier";
	
	/* Je maakt een bean van het type InMemoryUserDetailsManager. Je houdt met deze bean principals bij in het interne geheugen.
	 * Je leest ze verder in de cursus uit een database. */
	@Bean
	InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		
		return new InMemoryUserDetailsManager(
						User.builder().username("joe").password("{noop}theboss").authorities(MANAGER).build(),
						User.builder().username("averell").password("{noop}hungry").authorities(HELPDESKMEDEWERKER, MAGAZIJNIER).build());
		/*
		 * Je geeft aan de InMemoryUserDetailsManager constructor enkele Users (principals) mee.
		 * Je maakt een User met het builder design pattern. Je vermeldt de gebruikersnaam, het passwoord en de authorities.
		 * Voor het paswoord staat {noop}. Dit betekent dat Spring het paswoord niet-versleuteld onthoudt. Je leest verder in de cursus werken met versleutelde paswoorden.
		 */
		
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
		
		http.formLogin()	// De gebruiker authenticeert zich door zijn gebruikersnaam en paswoord te tikken in een HTML form.
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
