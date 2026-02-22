package com.example.project_rent_yard.config;

import com.example.project_rent_yard.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // có thể bật lại sau

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/owner/**")
                        .hasAuthority("owner")

                        // ADMIN only
                        .requestMatchers("/admins/**")
                        .hasAnyAuthority("admin", "owner")

                        // USER + admin
                        .requestMatchers("/clients/**", "/userInfo/**")
                        .hasAnyAuthority("user", "admin", "owner")

                        // public
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**")
                        .permitAll()

                        .anyRequest().authenticated()
                )

                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("name")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/clients", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .sessionManagement(session -> session
                        .maximumSessions(1) // chỉ 1 session
                )

                .userDetailsService(userDetailsService);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

