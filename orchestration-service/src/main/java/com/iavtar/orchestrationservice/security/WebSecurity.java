package com.iavtar.orchestrationservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true,
		jsr250Enabled = true,
		securedEnabled = true
		)
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final Environment environment;

	public WebSecurity(Environment environment) {
		this.environment = environment;
	}

	@Value("${api.signup.url}")
	private String signupUrl;
	
	@Value("${api.signin.url}")
	private String signinUrl;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/api/product/all-products").permitAll()
			.antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
//			.antMatchers("/hystrix").permitAll()
//			.antMatchers("actuator/hystrix.stream").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new AuthorizationFilter(authenticationManager(), environment));		
		
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}

}
