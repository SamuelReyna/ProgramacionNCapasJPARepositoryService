package com.programacionNCapas.SReynaProgramacionNCapas.RestController;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rol Controller")
@RestController
@RequestMapping("api/rol")
public class RolController {

    @Autowired
    private RolService rolService;

    /**
     * Obtiene todos los roles de la base de datos
     *
     * @return
     */
    @Operation(summary = "Obtener todos los roles", description = "Devuelve la lista completa de roles")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping()
    public ResponseEntity GetAll() {
        Result result = rolService.GetAll();
        return ResponseEntity.status(result.status).body(result);
    }
}
