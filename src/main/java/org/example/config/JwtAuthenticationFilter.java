package org.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.services.impl.AppUserService;
import org.example.services.impl.JwtSecurityService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtSecurityService jwtSecurityService;
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();


        if (path.startsWith("/api/auth/") ||
                path.startsWith("/v3/api-docs/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }


        String header = request.getHeader("Authorization");


        if (StringUtils.isEmpty(header) || !StringUtils.startsWith(header, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = header.substring(7);
        String email = jwtSecurityService.extractUsername(jwt);


        if (StringUtils.isNotEmpty(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = appUserService.loadUserByUsername(email);


            if (jwtSecurityService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
