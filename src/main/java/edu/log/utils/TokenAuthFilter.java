/*
 * TokenAuthFilter.java
 * 
 * This class is a custom filter that extends OncePerRequestFilter to handle token-based authentication. It checks for the presence of a token in the Authorization header, validates it, and sets the authentication in the SecurityContext if valid.
 */

package edu.log.utils;

import edu.log.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.io.IOException;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserService us;
    private final String HEADER_PREFIX = "Bearer "; 

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Check if the Authorization header is present and starts with the expected prefix
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(HEADER_PREFIX)) {
            String token = header.substring(HEADER_PREFIX.length());

            // Validate the token using the UserService
            if (us.isTokenValid(token)) {
                String email = us.getEmailFromToken(token);
                if (email != null) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(email, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
