package com.example.hotelreservationsysyemforweddings.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(customAuthenticationProvider)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // --- Publicly Accessible Resources & APIs ---
                        .requestMatchers(
                                "/", "/index.html", "/login.html", "/register.html",
                                "/images/**", "/fonts/**",
                                "/api/auth/register",
                                // START OF CHANGE: Added /api/ballrooms here and removed the old one
                                "/api/ballrooms",
                                "/api/packages",
                                "/api/bookings/check-availability",
                                "/api/vendors"
                                // END OF CHANGE
                        ).permitAll()

                        // --- Role-Based Admin Page Access ---
                        .requestMatchers("/admin-manage-users.html").hasRole("IT_OFFICER")
                        .requestMatchers("/manage-customers.html").hasAnyRole("HOTEL_MANAGER", "IT_OFFICER")
                        .requestMatchers("/manage-vendors.html").hasAnyRole("COORDINATOR", "HOTEL_MANAGER", "IT_OFFICER")
                        .requestMatchers("/admin-dashboard.html", "/manage-bookings.html").hasAnyRole("RECEPTIONIST", "COORDINATOR", "HOTEL_MANAGER", "IT_OFFICER")
                        .requestMatchers("/admin.html", "/ballrooms.html").hasAnyRole("COORDINATOR", "HOTEL_MANAGER", "IT_OFFICER")

                        // --- Role-Based Admin API Access ---
                        .requestMatchers("/api/manage/customers/**").hasAnyRole("HOTEL_MANAGER", "IT_OFFICER")

                        // --- Customer-only pages are now strictly for ROLE_CUSTOMER ---
                        .requestMatchers("/booking.html", "/reservations.html", "/profile.html").hasRole("CUSTOMER")

                        // --- General Authenticated APIs ---
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/index.html")
                        .permitAll());

        return http.build();
    }
}