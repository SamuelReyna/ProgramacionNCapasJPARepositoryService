package com.programacionNCapas.SReynaProgramacionNCapas.RestController;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Estado Controller")
@RestController
@RequestMapping("api/estado")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    /**
     * Obtiene todos los estados de la base de datos
     *
     * @return
     */
    @Operation(summary = "Obtener todos los estados", description = "Devuelve la lista completa de estados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping()
    public ResponseEntity GetAll() {
        Result result = estadoService.GetAll();
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Obtiene todos los paises de la base de datos por id pais
     *
     * @param IdPais
     * @return
     */
    @Operation(summary = "Obtener todos los estados dentro de un país", description = "Devuelve la lista de estados de un país")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("/byPais/{IdPais}")
    public ResponseEntity GetByPais(@PathVariable int IdPais) {
        Result result = estadoService.GetByPais(IdPais);
        return ResponseEntity.status(result.status).body(result);
    }

}
