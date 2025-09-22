package com.programacionNCapas.SReynaProgramacionNCapas.RestController;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.MunicipioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Municipio Controller")
@RestController
@RequestMapping("api/municipio")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    /**
     * Obtiene todos los municipios de la base de datos
     *
     * @return
     */
    @Operation(summary = "Obtener todos los municipios", description = "Devuelve la lista completa de municipios")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping()
    public ResponseEntity GetAll() {
        Result result = municipioService.GetAll();
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Obtiene todos los municipios de la base de datos de un estado
     *@param IdEstado
     * @return
     */
    @Operation(summary = "Obtener todos los municipio de un estado", description = "Devuelve la lista completa de municipios de un estado")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("/byEstado/{IdEstado}")
    public ResponseEntity GetByEstado(@PathVariable int IdEstado) {
        Result result = municipioService.GetByEstado(IdEstado);
        return ResponseEntity.status(result.status).body(result);
    }
}
