package ru.inrtu.backend.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.inrtu.backend.dto.SchoolchildDto;
import ru.inrtu.backend.service.SchoolchildService;
import ru.inrtu.backend.utils.JwtUtil;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String JWT_HEADER_KEY = "Authorization";

    private static final String JWT_PREFIX = "Bearer ";

    private final SchoolchildService schoolchildService;

    public JwtRequestFilter(final SchoolchildService schoolchildService) {
        this.schoolchildService = schoolchildService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(JWT_HEADER_KEY);

        final String username;

        if (authorizationHeader != null && authorizationHeader.startsWith(JWT_PREFIX)) {
            final String jwt = authorizationHeader.substring(JWT_PREFIX.length());
            username = JwtUtil.extractUsername(jwt);
        } else {
            username = null;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = schoolchildService.loadUserByUsername(username);
            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        chain.doFilter(request, response);
    }

}