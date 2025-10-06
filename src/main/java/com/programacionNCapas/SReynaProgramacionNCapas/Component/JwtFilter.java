package com.programacionNCapas.SReynaProgramacionNCapas.Component;

import com.programacionNCapas.SReynaProgramacionNCapas.Service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class JwtFilter extends GenericFilter {
    
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    
    public JwtFilter(JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request
                = (HttpServletRequest) req;
        HttpServletResponse response
                = (HttpServletResponse) res;
        String header = request.getHeader("Authorization");
        
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Jws<Claims> claims = jwtUtil.validateToken(token);
                
                UserDetails userDetails
                        = usuarioService.loadUserByUsername(claims.getBody().getSubject());
                
                UsernamePasswordAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                
                SecurityContextHolder.getContext().setAuthentication(auth);
                
            } catch (UsernameNotFoundException e) {
            }
        }
        
        chain.doFilter(req, res);
        
    }
    
}
