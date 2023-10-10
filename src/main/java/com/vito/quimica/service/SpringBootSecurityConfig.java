package com.vito.quimica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SpringBootSecurityConfig {

	@Autowired
	private UserDetailsServiceImpl userDetails;

	@Bean
	static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("unused")
	private void userDetails(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
	}

	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

		http.authorizeHttpRequests((authz) ->

		authz.requestMatchers(mvc.pattern("/"), 
				mvc.pattern("/css/**"),
				mvc.pattern("/js/**"), 
				mvc.pattern("/image/**"), 
				mvc.pattern("/usuario/**"),
				mvc.pattern("/images/**"),
				mvc.pattern("/layout/**"))
		.permitAll()
				
		.requestMatchers(
				mvc.pattern("/administrador/**"),
				mvc.pattern("/productos/**")) 
		
		.hasRole("ADMIN")
			
			
			
			//.hasRole("ADMIN")
							 
				.anyRequest().authenticated())
				.formLogin(login -> login.permitAll().loginPage("/usuario/login"))
				.logout(logout -> logout.permitAll());
		return http.build();
	}

}
