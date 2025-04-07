/*
 * SecurityConfig.java
 * 
 * This class configures the security settings for the application using Spring Security. It defines the security filter chain, access control, and exception handling for unauthorized access and access denied scenarios.
 */

 package edu.log.config;

 import edu.log.utils.TokenAuthFilter;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.access.AccessDeniedHandler;
 import org.springframework.security.web.AuthenticationEntryPoint;
 import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
 import org.springframework.security.config.http.SessionCreationPolicy;
 import jakarta.servlet.http.HttpServletResponse;
 
 @Configuration
 @EnableWebSecurity
 public class SecurityConfig {
 
     @Autowired
     private TokenAuthFilter taf;
 
     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
             .csrf(csrf -> csrf.disable())
             .authorizeHttpRequests(auth -> auth
                 .requestMatchers(
                     "/api/auth/token",
                     "/api/auth/create",
                     "/h2-console/**"
                 ).permitAll()
                 .anyRequest().authenticated()
             )
             .headers(headers -> headers
                 .frameOptions(frame -> frame.disable()) // Allow H2 Console
             )
             .exceptionHandling(ex -> ex
                 .accessDeniedHandler(accessDeniedHandler())
                 .authenticationEntryPoint(authenticationEntryPoint())
             )
             .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .addFilterBefore(taf, UsernamePasswordAuthenticationFilter.class);
 
         return http.build();
     }
 
     @Bean
     public AccessDeniedHandler accessDeniedHandler() {
         return (request, response, accessDeniedException) -> {
             response.setStatus(HttpServletResponse.SC_FORBIDDEN);
             response.setContentType("application/json");
             response.getWriter().write("{\"error\": \"Access denied: You do not have permission to access this resource.\"}");
         };
     }
 
     @Bean
     public AuthenticationEntryPoint authenticationEntryPoint() {
         return (request, response, authException) -> {
             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
             response.setContentType("application/json");
             response.getWriter().write("{\"error\": \"Unauthorized: You must provide a valid token to access this resource.\"}");
         };
     }
 }
 