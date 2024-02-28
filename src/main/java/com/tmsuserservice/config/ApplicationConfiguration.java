package com.tmsuserservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class ApplicationConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.sessionManagement(
          managment ->managment.sessionCreationPolicy(
                  SessionCreationPolicy.STATELESS
          )
        ).authorizeHttpRequests(
                Authorize -> Authorize.requestMatchers("/api/**").authenticated().anyRequest().permitAll()
        ).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf->csrf.disable())
                .cors(cors-> cors.configurationSource(corsConfiguration()))
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }


    private CorsConfigurationSource corsConfiguration() {
        return new CorsConfigurationSource(){
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(Collections.singletonList("*"));
                cfg.setAllowedMethods(Collections.singletonList("*"));
                cfg.setAllowCredentials(true);
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setExposedHeaders(Arrays.asList("Authorization"));
                cfg.setMaxAge(3600L);
                return cfg;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

/*
Annotations:

Annotated with @Configuration, indicating that this class declares one or more @Bean methods and can be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.
Methods:

securityFilterChain:

Annotated with @Bean, indicating that it produces a bean to be managed by the Spring container.
Configures security filters using the provided HttpSecurity object.
Sets session management to use stateless sessions.
Defines authorization rules, allowing authenticated access to URLs under "/api/**" and permitting access to any other request.
Adds a JwtTokenValidator filter before the BasicAuthenticationFilter.
Disables CSRF protection.
Configures CORS (Cross-Origin Resource Sharing) using the corsConfiguration() method.
Configures HTTP Basic authentication and form-based login with default settings.
Returns a SecurityFilterChain built from the configured HttpSecurity.
corsConfiguration:

Defines a private method that configures CORS settings.
Returns a CorsConfigurationSource that allows requests from any origin, with any method, and with any headers. It also exposes the "Authorization" header and sets a max age for pre-flight requests.
passwordEncoder:

Annotated with @Bean, indicating that it produces a bean to be managed by the Spring container.
Creates and returns a BCryptPasswordEncoder, which is used for hashing passwords securely.
Overall, this ApplicationConfiguration class configures security filters, CORS configuration, and password encoding for the Spring Boot application. It promotes stateless session management and sets up various security features according to the application's requirements.
*/
