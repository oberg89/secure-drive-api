package se.securedrive.security;

import se.securedrive.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.JwtException;
import se.securedrive.model.User;
import se.securedrive.service.UserService;

import java.io.IOException;
import java.util.Collections;

/**
 * Filter som körs för varje request för att kontrollera om det finns en giltig JWT-token.
 * Om en giltig token hittas, autentiseras användaren i Spring Security-kontexten.
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Kontrollera om Authorization-headern innehåller en Bearer-token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);

                // Hämta användaren från databasen baserat på namnet i token
                // Vi använder userRepository direkt för att undvika Exception om användaren inte finns
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null) {
                    request.setAttribute("user", user);

                    // Om ingen autentisering finns i kontexten, sätt den nu
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    // Om användaren inte finns i databasen (men token var giltig), rensa kontexten
                    SecurityContextHolder.clearContext();
                }
            } catch (JwtException ex) {
                // Vid felaktig token rensas kontexten. Vi skickar inte 401 direkt här
                // för att tillåta filterkedjan att fortsätta (t.ex. för publika endpoints)
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
