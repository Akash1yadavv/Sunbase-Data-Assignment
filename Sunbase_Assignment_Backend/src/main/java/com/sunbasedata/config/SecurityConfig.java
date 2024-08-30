package com.sunbasedata.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired private AuthenticationProvider authenticationProvider;
	@Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
	    http
	    .csrf(csrf -> csrf.disable())
	    .cors(Customizer.withDefaults())
	    .authorizeHttpRequests(auth -> auth
	        .requestMatchers("/api/sunbasedata/auth/**").permitAll()
	        .requestMatchers("/swagger-ui*/**", "/v3/api-docs/**").permitAll()
	        .requestMatchers("/api/sunbasedata/get-customer/**", "/api/sunbasedata/search-customers/**").hasAnyRole("USER","ADMIN")
	        .requestMatchers("/api/sunbasedata/customers-list/**", "/api/sunbasedata/register-customer/**").hasAnyRole("USER","ADMIN")
	        .anyRequest().hasRole("ADMIN")
	    )
	    .sessionManagement(session -> session
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set session management to stateless
	    )
	    .authenticationProvider(authenticationProvider) // Set the custom authentication provider
	    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT authentication filter before UsernamePasswordAuthenticationFilter
	
	    return http.build();
	    
	}   
    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${allowedOrigins}") String allowedOriginsString) {
        List<String> allowedOriginsList = Arrays.asList(allowedOriginsString.split(","));
        
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOriginsList);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type","Content-type","*"));
        configuration.setExposedHeaders(Arrays.asList("*","X-Get-Header"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }


 
}
