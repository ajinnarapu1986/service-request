package com.sr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

	@Autowired
	private UserDetailsService uds;

	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * Configuring the Spring Security for Request Matchers
	 * 
	 * @param HttpSecurity
	 * @return SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/content/**", "/accessDenied").permitAll()
				.anyRequest().authenticated()
			).formLogin(
			        form -> form
			                .loginPage("/login")
			                .loginProcessingUrl("/login")
			                .defaultSuccessUrl("/request-info/list", true)
			                .permitAll()
			).logout(
			        logout -> logout
			                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			                .invalidateHttpSession(true)
			                .deleteCookies("JSESSIONID")
			                .permitAll()
			)
			.exceptionHandling().accessDeniedPage("/accessDenied").and()
			.authenticationProvider(authenticationProvider()); 
        
        return http.build();
	}

	/**
	 * DaoAuthenticationProvider with BCrypt Password Encoder
	 * @return AuthenticationProvider
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(uds);
		authenticationProvider.setPasswordEncoder(encoder);
		return authenticationProvider;
	}
}