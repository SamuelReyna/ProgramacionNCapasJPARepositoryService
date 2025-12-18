package com.programacionNCapas.SReynaProgramacionNCapas.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter extends GenericFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    public JwtFilter(JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        // ✅ Ignorar las rutas públicas
        if (path.startsWith("/auth/") || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        // ✅ Si no hay header de autorización en rutas protegidas
        if (header == null || !header.startsWith("Bearer ")) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "No se proporcionó token de autenticación");
            return;
        }

        String token = header.substring(7);

        try {
            // ✅ Validar el token
            Jws<Claims> claims = jwtUtil.validateToken(token);

            // ✅ Cargar el usuario
            UserDetails userDetails = usuarioService.loadUserByUsername(
                    claims.getBody().getSubject()
            );

            // ✅ Establecer la autenticación
            UsernamePasswordAuthenticationToken auth
                    = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);

            // ✅ Continuar con la cadena de filtros
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            // ✅ Token expirado - 401 Unauthorized
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "El token ha expirado");

        } catch (SignatureException e) {
            // ✅ Firma inválida - 401 Unauthorized
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Firma del token inválida");

        } catch (MalformedJwtException e) {
            // ✅ Token malformado - 401 Unauthorized
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Token malformado");

        } catch (UnsupportedJwtException e) {
            // ✅ Token no soportado - 401 Unauthorized
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Token no soportado");

        } catch (IllegalArgumentException e) {
            // ✅ Token vacío o nulo - 401 Unauthorized
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Token vacío o inválido");

        } catch (UsernameNotFoundException e) {
            // ✅ Usuario no encontrado - 401 Unauthorized
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Usuario no encontrado");
        } catch (Exception e) {
            // ✅ Cualquier otro error - 500 Internal Server Error
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error interno al procesar el token");
        }
    }

    /**
     * Método auxiliar para enviar respuestas de error en formato JSON
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("correct", false);
        errorDetails.put("status", status);
        errorDetails.put("errorMessage", message);
        errorDetails.put("timestamp", System.currentTimeMillis());

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
