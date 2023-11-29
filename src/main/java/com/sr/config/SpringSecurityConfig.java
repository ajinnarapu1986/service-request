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

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests().requestMatchers("/", "/content/**", "/home", "/register", "/saveUser").permitAll()
				.requestMatchers("/request-info").authenticated().requestMatchers("/admin").hasAuthority("Admin")
				.requestMatchers("/mgr").hasAuthority("Manager").requestMatchers("/emp").hasAuthority("Employee")
				.requestMatchers("/hr").hasAuthority("HR").requestMatchers("/common")
				.hasAnyAuthority("Employeee", "Manager", "Admin").anyRequest().authenticated()

				//.and().formLogin().defaultSuccessUrl("/welcome", true)
				.and().formLogin(
                        form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        //.failureUrl("/login-error")
                        .defaultSuccessUrl("/request-info/list", true)
                        .permitAll()
        )

				//.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                ).exceptionHandling().accessDeniedPage("/accessDenied")
				.and().authenticationProvider(authenticationProvider());

		return http.build();

	}
	
//	@Bean
//	public WebSecurityCustomizer apiStaticResources() {
//	    return (web)->web.ignoring().requestMatchers("/content/**");
//	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(uds);
		authenticationProvider.setPasswordEncoder(encoder);
		return authenticationProvider;
	}
}