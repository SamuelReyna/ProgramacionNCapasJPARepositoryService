package com.programacionNCapas.SReynaProgramacionNCapas.RestController;

import com.programacionNCapas.SReynaProgramacionNCapas.Component.JwtUtil;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Password;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.EmailService;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.PasswordResetTokenService;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.UsuarioService;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.VerifyTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private EmailService emailService;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final PasswordResetTokenService passwordResetTokenService;

    private final VerifyTokenService verifyTokenService;

    public AuthController(PasswordResetTokenService passwordResetTokenService,
            com.programacionNCapas.SReynaProgramacionNCapas.Component.JwtUtil jwtUtil,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
            VerifyTokenService verifyTokenService) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenService = passwordResetTokenService;
        this.verifyTokenService = verifyTokenService;
    }

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity GetByUsername(@RequestBody UsuarioJPA usuario) {

        Result result = usuarioService.GetByUsername(usuario.getUsername());
        UserDetails user = (UserDetails) result.object;
        UsuarioJPA u = (UsuarioJPA) result.object;
        if (user == null || !passwordEncoder.matches(usuario.getPassword(), user.getPassword())) {
            HashMap<String, Object> message = new HashMap();
            message.put("errorMessage", "Usuario o contraseña incorrectos");

            return ResponseEntity.badRequest().body(message);
        }
        if (u.getVerify() == 1 && u.getEstatus() == 1) {
            String token = verifyTokenService.GenerateToken(u.getIdUser());
            return ResponseEntity.status(result.status).body(token);
        }
        String jwt = jwtUtil.generateToken(user.getUsername(), user.getAuthorities().toString());

        HashMap<String, Object> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.status(result.status).body(response);
    }

    @GetMapping("/decode")
    public Map<String, Object> Decode(@RequestHeader("Authorization") String header) {
        if (header != null && header.startsWith("Bearer ")) {
            String jwt = header.substring(7);
            Jws<Claims> claims = jwtUtil.validateToken(jwt);
            return claims.getBody();
        } else {
            throw new IllegalArgumentException("Token inválido o ausente");
        }
    }

    @PostMapping("/sendEmail")
    public ResponseEntity sendEmail(@RequestParam("email") String email) throws Exception {

        Result result = usuarioService.loadUserByEmail(email);
        if (result.correct) {
            if (result.object != null) {
                UsuarioJPA usuario = (UsuarioJPA) result.object;

                String token = passwordResetTokenService.GenerarToken(usuario.getIdUser());

                String linkRestablecer = "http://localhost:8080/changePassword?token=" + token;

                String html = """
                              <!DOCTYPE html>
                              <html lang="es">
                              <head>
                                  <meta charset="UTF-8">
                                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                  <title>Restablecer Contrase\u00f1a</title>
                                  <style>
                                      body {
                                          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                          background-color: #f4f4f4;
                                          margin: 0;
                                          padding: 0;
                                      }
                                      .container {
                                          max-width: 600px;
                                          margin: 40px auto;
                                          background-color: #ffffff;
                                          border-radius: 10px;
                                          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                          overflow: hidden;
                                      }
                                      .header {
                                          background-color: #007bff;
                                          color: #ffffff;
                                          padding: 20px;
                                          text-align: center;
                                      }
                                      .content {
                                          padding: 30px;
                                          color: #333333;
                                          line-height: 1.6;
                                      }
                                      .button {
                                          display: inline-block;
                                          padding: 12px 24px;
                                          margin: 20px 0;
                                          background-color: #007bff;
                                          color: #ffffff;
                                          text-decoration: none;
                                          border-radius: 6px;
                                          font-weight: bold;
                                          transition: background-color 0.3s ease;
                                      }
                                      .button:hover {
                                          background-color: #0056b3;
                                      }
                                      .footer {
                                          text-align: center;
                                          font-size: 12px;
                                          color: #999999;
                                          padding: 20px;
                                          border-top: 1px solid #eeeeee;
                                      }
                                  </style>
                              </head>
                              <body>
                                  <div class="container">
                                      <div class="header">
                                          <h1>\u00a1Hola """ + usuario.getUsername() + "!</h1>\n"
                        + "        </div>\n"
                        + "        <div class=\"content\">\n"
                        + "            <p>Recibimos una solicitud para restablecer tu contraseña.</p>\n"
                        + "            <p>Haz clic en el botón de abajo para crear una nueva contraseña segura:</p>\n"
                        + "            <a href=\"" + linkRestablecer + "\" class=\"button\">Restablecer Contraseña</a>\n"
                        + "            <p>Si no realizaste esta solicitud, puedes ignorar este mensaje. Tu cuenta seguirá siendo segura.</p>\n"
                        + "        </div>\n"
                        + "        <div class=\"footer\">\n"
                        + "            &copy; 2025 TuAplicación. Todos los derechos reservados.\n"
                        + "        </div>\n"
                        + "    </div>\n"
                        + "</body>\n"
                        + "</html>";

                emailService.sendEmail(usuario.getEmail(), "Solicitud de recuperación de contraseña", html);

            }
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @PatchMapping("/changePass")
    public ResponseEntity changePass(@RequestBody Password password, @RequestParam("token") String token) {
        Result result = new Result();
        if (token != null) {
            if (passwordResetTokenService.validarToken(token)) {
                int idUser = passwordResetTokenService.getUserIdbyToken(token);

                if (idUser > 0) {
                    result = usuarioService.GetById(idUser);
                    if (result.correct) {
                        UsuarioJPA usuario = (UsuarioJPA) result.object;
                        if (password.getPassword() == null ? password.getConfirmPassword() == null : password.getPassword().equals(password.getConfirmPassword())) {
                            usuario.setPassword(passwordEncoder.encode(password.getPassword()));
                            Result resultUpdate = usuarioService.Update(usuario);
                            if (!resultUpdate.correct) {
                                result = resultUpdate;
                            } else {
                                String linkRestablecer = "http://localhost:8080/changePassword?token=" + token;

                                String html = """
                                              <!DOCTYPE html>
                                              <html lang="es">
                                              <head>
                                                  <meta charset="UTF-8">
                                                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                                  <title>Contrase\u00f1a Actualizada</title>
                                                  <style>
                                                      body {
                                                          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                                          background-color: #f4f4f4;
                                                          margin: 0;
                                                          padding: 0;
                                                      }
                                                      .container {
                                                          max-width: 600px;
                                                          margin: 40px auto;
                                                          background-color: #ffffff;
                                                          border-radius: 10px;
                                                          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                                          overflow: hidden;
                                                      }
                                                      .header {
                                                          background-color: #28a745;
                                                          color: #ffffff;
                                                          padding: 20px;
                                                          text-align: center;
                                                      }
                                                      .content {
                                                          padding: 30px;
                                                          color: #333333;
                                                          line-height: 1.6;
                                                      }
                                                      .button {
                                                          display: inline-block;
                                                          padding: 12px 24px;
                                                          margin: 20px 0;
                                                          background-color: #28a745;
                                                          color: #ffffff;
                                                          text-decoration: none;
                                                          border-radius: 6px;
                                                          font-weight: bold;
                                                          transition: background-color 0.3s ease;
                                                      }
                                                      .button:hover {
                                                          background-color: #218838;
                                                      }
                                                      .footer {
                                                          text-align: center;
                                                          font-size: 12px;
                                                          color: #999999;
                                                          padding: 20px;
                                                          border-top: 1px solid #eeeeee;
                                                      }
                                                  </style>
                                              </head>
                                              <body>
                                                  <div class="container">
                                                      <div class="header">
                                                          <h1>Contrase\u00f1a Actualizada</h1>
                                                      </div>
                                                      <div class="content">
                                                          <p>\u00a1Hola   """ // Verde éxito
                                        + usuario.getUsername() + "!</p>\n"
                                        + "            <p>Queremos informarte que tu contraseña ha sido cambiada exitosamente.</p>\n"
                                        + "            <p>Si tú realizaste este cambio, no necesitas hacer nada más.</p>\n"
                                        + "            <p>Si <strong>no fuiste tú</strong>, te recomendamos restablecer tu contraseña de inmediato o contactar con el soporte técnico.</p>\n"
                                        + "        </div>\n"
                                        + "        <div class=\"footer\">\n"
                                        + "            &copy; 2025 TuAplicación. Todos los derechos reservados.\n"
                                        + "        </div>\n"
                                        + "    </div>\n"
                                        + "</body>\n"
                                        + "</html>";
                                emailService.sendEmail(usuario.getEmail(), "Actualización de contraseña", html);
                            }

                        }
                    }
                }

            } else {
                result.status = 400;
                result.errorMessage = "Token expirado, solicita otro";
            }
        } else {
            result.status = 400;
            result.errorMessage = "Token expirado, solicita otro";

        }
        return ResponseEntity.status(result.status).body(result);
    }

    @PostMapping("/sendVerifyEmail")
    public ResponseEntity<?> sendVerifyEmail(@RequestParam("email") String email) {
        Result result = usuarioService.loadUserByEmail(email);
        if (result.correct) {
            UsuarioJPA usuario = (UsuarioJPA) result.object;
            String token = verifyTokenService.GenerateToken(usuario.getIdUser());

            // 🔗 Enlace de verificación
            String linkVerificar = "http://localhost:8080/auth/verifyAccount?token=" + token;

            String html = """
                          <!DOCTYPE html>
                          <html lang="es">
                          <head>
                              <meta charset="UTF-8">
                              <meta name="viewport" content="width=device-width, initial-scale=1.0">
                              <title>Verificaci\u00f3n de Cuenta</title>
                              <style>
                                  body {
                                      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                      background-color: #f4f4f4;
                                      margin: 0;
                                      padding: 0;
                                  }
                                  .container {
                                      max-width: 600px;
                                      margin: 40px auto;
                                      background-color: #ffffff;
                                      border-radius: 10px;
                                      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                      overflow: hidden;
                                  }
                                  .header {
                                      background-color: #28a745;
                                      color: #ffffff;
                                      padding: 20px;
                                      text-align: center;
                                  }
                                  .content {
                                      padding: 30px;
                                      color: #333333;
                                      line-height: 1.6;
                                  }
                                  .button {
                                      display: inline-block;
                                      padding: 12px 24px;
                                      margin: 20px 0;
                                      background-color: #28a745;
                                      color: #ffffff;
                                      text-decoration: none;
                                      border-radius: 6px;
                                      font-weight: bold;
                                      transition: background-color 0.3s ease;
                                  }
                                  .button:hover {
                                      background-color: #1e7e34;
                                  }
                                  .footer {
                                      text-align: center;
                                      font-size: 12px;
                                      color: #999999;
                                      padding: 20px;
                                      border-top: 1px solid #eeeeee;
                                  }
                              </style>
                          </head>
                          <body>
                              <div class="container">
                                  <div class="header">
                                      <h1>\u00a1Bienvenido """ + usuario.getUsername() + "!</h1>\n"
                    + "        </div>\n"
                    + "        <div class=\"content\">\n"
                    + "            <p>Gracias por registrarte en nuestra aplicación.</p>\n"
                    + "            <p>Antes de comenzar, necesitamos verificar tu dirección de correo electrónico.</p>\n"
                    + "            <p>Haz clic en el siguiente botón para activar tu cuenta:</p>\n"
                    + "            <a href=\"" + linkVerificar + "\" class=\"button\">Verificar mi cuenta</a>\n"
                    + "            <p>Si no creaste esta cuenta, puedes ignorar este mensaje.</p>\n"
                    + "        </div>\n"
                    + "        <div class=\"footer\">\n"
                    + "            &copy; 2025 TuAplicación. Todos los derechos reservados.\n"
                    + "        </div>\n"
                    + "    </div>\n"
                    + "</body>\n"
                    + "</html>";

            emailService.sendEmail(
                    usuario.getEmail(),
                    "Verificación de cuenta - TuAplicación",
                    html
            );

            return ResponseEntity.ok("Correo de verificación enviado correctamente a " + usuario.getEmail());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró un usuario con el correo proporcionado.");
    }

    @GetMapping("/verifyAccount")
    public ResponseEntity Verify(@RequestParam("token") String token) {
        Result result = new Result();

        if (verifyTokenService.validarToken(token)) {
            result = (Result) usuarioService.GetById(verifyTokenService.getUserIdbyToken(token));
            if (result.correct) {
                UsuarioJPA usuario = (UsuarioJPA) result.object;

                usuarioService.Verify(usuario.getIdUser());
                String html = """
                                              <!DOCTYPE html>
                                              <html lang="es">
                                              <head>
                                                  <meta charset="UTF-8">
                                                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                                  <title>Cuenta Validada</title>
                                                  <style>
                                                      body {
                                                          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                                          background-color: #f4f4f4;
                                                          margin: 0;
                                                          padding: 0;
                                                      }
                                                      .container {
                                                          max-width: 600px;
                                                          margin: 40px auto;
                                                          background-color: #ffffff;
                                                          border-radius: 10px;
                                                          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                                          overflow: hidden;
                                                      }
                                                      .header {
                                                          background-color: #28a745;
                                                          color: #ffffff;
                                                          padding: 20px;
                                                          text-align: center;
                                                      }
                                                      .content {
                                                          padding: 30px;
                                                          color: #333333;
                                                          line-height: 1.6;
                                                      }
                                                      .button {
                                                          display: inline-block;
                                                          padding: 12px 24px;
                                                          margin: 20px 0;
                                                          background-color: #28a745;
                                                          color: #ffffff;
                                                          text-decoration: none;
                                                          border-radius: 6px;
                                                          font-weight: bold;
                                                          transition: background-color 0.3s ease;
                                                      }
                                                      .button:hover {
                                                          background-color: #218838;
                                                      }
                                                      .footer {
                                                          text-align: center;
                                                          font-size: 12px;
                                                          color: #999999;
                                                          padding: 20px;
                                                          border-top: 1px solid #eeeeee;
                                                      }
                                                  </style>
                                              </head>
                                              <body>
                                                  <div class="container">
                                                      <div class="header">
                                                          <h1>Contrase\u00f1a Actualizada</h1>
                                                      </div>
                                                      <div class="content">
                                                          <p>\u00a1Hola   """ // Verde éxito
                        + usuario.getUsername() + "!</p>\n"
                        + "            <p>Tu cuenta ha sido validada.</p>\n"
                        + "            <p>Si tú realizaste este cambio, no necesitas hacer nada más.</p>\n"
                        + "        </div>\n"
                        + "        <div class=\"footer\">\n"
                        + "            &copy; 2025 TuAplicación. Todos los derechos reservados.\n"
                        + "        </div>\n"
                        + "    </div>\n"
                        + "</body>\n"
                        + "</html>";
                emailService.sendEmail(usuario.getEmail(), "Cuenta validada", html);
            }
        }

        return ResponseEntity.status(result.status).body(result);
    }

}
