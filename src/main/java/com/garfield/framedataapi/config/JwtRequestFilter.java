package com.garfield.framedataapi.config;

import com.garfield.framedataapi.users.User;
import com.garfield.framedataapi.users.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final UsersService usersService;

    public JwtRequestFilter(GoogleTokenVerifier googleTokenVerifier, UsersService usersService) {
        this.googleTokenVerifier = googleTokenVerifier;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String idToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            idToken = authorizationHeader.substring(7);
        }

        if (idToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var payload = googleTokenVerifier.verify(idToken);
            if (payload != null) {
                String userEmail = payload.getEmail();

                Optional<User> userOptional = usersService.findByEmail(userEmail);

                userOptional.ifPresent(user -> {
                    var authorities = usersService.getAuthorities(user);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            authorities
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });

            }
        }

        filterChain.doFilter(request, response);
    }

}