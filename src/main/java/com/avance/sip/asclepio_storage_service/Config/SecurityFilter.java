package com.avance.sip.asclepio_storage_service.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final EmpresaContext empresaContext;

    public SecurityFilter(TokenService tokenService, EmpresaContext empresaContext) {
        this.tokenService = tokenService;
        this.empresaContext = empresaContext;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = recoverToken(request);

            if (token != null) {

                var decodedJWT = tokenService.validateToken(token);

                String username = decodedJWT.getSubject();

                Long empresaId = decodedJWT
                        .getClaim("empresaId")
                        .asLong();

                List<String> permissions = decodedJWT
                        .getClaim("permissions")
                        .asList(String.class);

                var authorities = permissions == null
                        ? List.<SimpleGrantedAuthority>of()
                        : permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                if (username != null && empresaId != null) {

                    empresaContext.setEmpresaId(empresaId);

                    var authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);

        } finally {
            empresaContext.limpar();
        }
    }

    private String recoverToken(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.replace("Bearer ", "").trim();
    }
}