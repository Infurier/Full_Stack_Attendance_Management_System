package com.example.attendance_management.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;
@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final String frontendOrigin;

    public SecurityConfig(JwtUtil jwtUtil, @Value("${app.frontend.origin}") String frontendOrigin) {
        this.jwtUtil = jwtUtil;
        this.frontendOrigin = frontendOrigin;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil);

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(c -> c.configurationSource(corsConfig()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // Students endpoints: ADMIN for create/update/delete; everyone authenticated for read
                .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")
                .requestMatchers("/api/students/export").hasRole("ADMIN")
                // Attendance: teachers & admin can mark; authenticated can view
                .requestMatchers("/api/attendance/mark").hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers("/api/attendance/export").hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers("/api/**").authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // for h2 console
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfig() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(frontendOrigin));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    // simple in-memory users for demo; in production use persistent user store
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin").password(encoder.encode("adminpass")).roles("ADMIN").build();
        UserDetails teacher = User.withUsername("teacher").password(encoder.encode("teacherpass")).roles("TEACHER").build();
        return new InMemoryUserDetailsManager(admin, teacher);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
