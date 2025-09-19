package com.programacionNCapas.SReynaProgramacionNCapas.RestController;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.ColoniaDAOJPAImplementation;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Colonia Controller")
@RestController
@RequestMapping("api/colonia")
public class ColoniaController {

    @Autowired
    private ColoniaDAOJPAImplementation coloniaDAOJPAImplementation;

    /**
     * Obtiene todas las colonias de la base de datos
     *
     * @return
     */
    @Operation(summary = "Obtener todos las colonias", description = "Devuelve la lista completa de colonias")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping()
    public ResponseEntity GetAll() {
        Result result = coloniaDAOJPAImplementation.GetAll();
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Obtiene todas los colonias de la base de datos por municipio
     *@param IdMunicipio
     * @return
     */
    @Operation(summary = "Obtener todas las colonias de un municipio", description = "Devuelve la lista completa de colonias por un municipio")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("byMunicipio/{IdMunicipio}")
    public ResponseEntity GetByMunicipio(@PathVariable int IdMunicipio) {
        Result result = coloniaDAOJPAImplementation.GetByIdMunicipio(IdMunicipio);
        return ResponseEntity.status(result.status).body(result);
    }
}
