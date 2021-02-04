package lendingplace.library.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lendingplace.library.service.LibrarianDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private final static String[] publicEndpoints = {
			"/accounts/**",
			"/browse/**",
			"/search/**",
			"/reserve"
	};
	private final static String[] privateEndpoints = {
			"/add/**",
			"/update/**",
			"/delete/**",
			"/checkout",
			"/return"
	};
	
	@Autowired
	private LibrarianDetailsService detailsService;
	
	@Autowired
	private JwtEntryPoint entryPoint;

	@Bean
	public JwtFilter getJwtFilter() {
		return new JwtFilter();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailsService).passwordEncoder(passwordEncoder());
		super.configure(auth);
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling()
		.authenticationEntryPoint(entryPoint).and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().antMatchers(publicEndpoints).permitAll()
		.antMatchers(privateEndpoints).permitAll().anyRequest().authenticated();
		http.addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
}
