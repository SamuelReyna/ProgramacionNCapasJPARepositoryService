package com.programacionNCapas.SReynaProgramacionNCapas.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permite todas las rutas
                        .allowedOrigins("**","http://localhost:8081", "http://localhost:8082") // Orígenes permitidos
                        .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                        .allowedHeaders("*") // Permite todos los encabezados
                        .allowCredentials(true); // Permite el uso de credenciales
            }
        };
    }
}
