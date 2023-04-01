package dev.joncollins.mpapi.configuration;

import dev.joncollins.mpapi.auth.AuthService;
import dev.joncollins.mpapi.repositories.TokenRepository;
import dev.joncollins.mpapi.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest req,
           @NonNull HttpServletResponse res,
           @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = req.getHeader("Authorization");
        final String userEmail;
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(req, res);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userRepo.findUserByEmail(userEmail).orElse(null);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(req, res);
    }
}
