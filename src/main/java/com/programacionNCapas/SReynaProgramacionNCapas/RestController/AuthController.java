package com.programacionNCapas.SReynaProgramacionNCapas.RestController;

import com.programacionNCapas.SReynaProgramacionNCapas.Component.JwtUtil;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.UsuarioService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(com.programacionNCapas.SReynaProgramacionNCapas.Component.JwtUtil jwtUtil, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity GetByUsername(@RequestBody UsuarioJPA usuario) {

        Result result = usuarioService.GetByUsername(usuario.getUsername());
        UserDetails user = (UserDetails) result.object;

        if (user == null || !passwordEncoder.matches(usuario.getPassword(), user.getPassword())) {
            HashMap<String, Object> message = new HashMap();
            message.put("errorMessage", "Usuario o contraseña incorrectos");

            return ResponseEntity.badRequest().body(message);
        }
        String jwt = jwtUtil.generateToken(user.getUsername(), user.getAuthorities().toString());
    
        HashMap<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("username", user.getUsername());
        response.put("roles", user.getAuthorities());
        return ResponseEntity.status(result.status).body(response);
    }

}
