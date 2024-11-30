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

                        .requestMatchers(HttpMethod.GET, "/api/orders/all").hasRole("ADMIN") // Доступ к getAllOrders только для ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/orders/user").authenticated() // Доступ к getOrdersByUserId для аутентифицированных пользователей
                        .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}/status").hasRole("ADMIN") // Доступ к updateOrderStatus только для ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/orders/{orderId}").authenticated() // Доступ к getOrderById для аутентифицированных пользователей
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/{orderId}/cancel").authenticated() // Доступ к cancelOrder для аутентифицированных пользователей
                        .requestMatchers(HttpMethod.GET, "/api/orders/status").hasRole("ADMIN") // Доступ к getOrdersByStatus только для ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/orders/createdAfter").hasRole("ADMIN") // Доступ к getOrdersCreatedAfter только для ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/orders/deliveryMethod").hasRole("ADMIN") // Доступ к getOrdersByDeliveryMethod только для ADMIN

                        // Открытый доступ к просмотру продуктов
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        
                        // Доступ к изменению, созданию и удалению только для администраторов
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/promotions/**").permitAll() // Только GET доступен всем
                        .requestMatchers("/api/promotions/**").hasRole("ADMIN") // Остальные методы только для ADMIN

                        // Разрешаем администраторам доступ к GET /api/users и GET /api/users/{id}/details
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}/details").hasRole("ADMIN")
                        // Разрешаем аутентифицированным пользователям доступ к GET /api/users/{id}
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").authenticated()
                        // Разрешаем аутентифицированным пользователям доступ к PUT и DELETE /api/users/{id}
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").authenticated()
                        // Разрешаем администраторам создавать новых пользователей
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        // Все остальные запросы к /api/users/** требуют роль ADMIN
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