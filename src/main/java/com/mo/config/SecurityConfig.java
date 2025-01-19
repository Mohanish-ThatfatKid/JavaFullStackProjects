package com.mo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.mo.service.impl.ICustomUserAuthServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private ICustomUserAuthServiceImpl customUserAuthServiceImpl;

	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.userDetailsService(customUserAuthServiceImpl)
	        .passwordEncoder(passwordEncoder()); // Ensure the encoder is used here
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		System.out.println("SecurityConfig.filterChain()");
		http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/auth/**", "/auth/verify/**", "/api/property/*/review", "/auth/userSignin",
								"/api/auth/hostSignin").permitAll()
						.requestMatchers("/api/user/**","/api/booking/user/**","/api/complaint/user/**").hasAuthority("ROLE_USER")
						.requestMatchers("/api/host/**","/api/booking/host/**","/api/complaint/user/**").hasAuthority("ROLE_HOST").anyRequest().authenticated())
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
				.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()));

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		System.out.println("SecurityConfig.corsConfigurationSource()");
		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);
		return request -> configuration;
	}
}
