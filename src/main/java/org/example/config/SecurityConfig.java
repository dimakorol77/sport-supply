package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.services.impl.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Обратите внимание, что @EnableGlobalMethodSecurity устарела, используйте @EnableMethodSecurity
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// Импортируем необходимые классы для фильтров
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppUserService appUserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/brands/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/discounts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/discounts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/discounts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/discounts/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/images/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/orders/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/user").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/{orderId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/{orderId}/cancel").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/orders/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/createdAfter").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/deliveryMethod").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        

                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/promotions/**").permitAll()
                        .requestMatchers("/api/promotions/**").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}/details").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").authenticated()

                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")

                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(appUserService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}