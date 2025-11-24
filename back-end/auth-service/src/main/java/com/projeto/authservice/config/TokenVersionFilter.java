package com.projeto.authservice.config;

import com.projeto.authservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenVersionFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public TokenVersionFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            var jwt = jwtToken.getToken();
            var userId = Long.parseLong(jwt.getSubject());
            var tokenVersion = jwt.getClaimAsString("version");

            if (tokenVersion == null) {
                throw new RuntimeException("Token version is null");
            }

            var currentTokenVersion = userRepository.findTokenVersionByUserId(userId);

            if (!currentTokenVersion.toString().equalsIgnoreCase(tokenVersion)) {
                deny(response);
                return;
            }
        }


        filterChain.doFilter(request, response);
    }

    private void deny(HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("""
                {
                    "error": "invalid_token_version"
                }
                """);
    }
}
