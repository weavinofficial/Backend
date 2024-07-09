package Weavin.Configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import Weavin.Services.JwtService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    
    private final AuthenticationManager authenticationManager;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println(authenticationManager);

        http
                .csrf(csrf -> {
                    csrf.disable();
                });

        http
                .sessionManagement(customizer -> {
                    customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .formLogin(customizer -> {
                    customizer.disable();
                })
                .httpBasic(customizer -> {
                    customizer.disable();
                })
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager))
                .authorizeHttpRequests(customizer -> {
                    customizer.requestMatchers("/signup").permitAll();
                    //UNVERIFIED role for any request should be removed at prod version, for now it's added for testing purpose as email verification is not yet implemented
                    customizer.anyRequest().hasAnyRole("ADMIN", "USER", "UNVERIFIED"); 
                });

        return http.build();
    }
}

    
