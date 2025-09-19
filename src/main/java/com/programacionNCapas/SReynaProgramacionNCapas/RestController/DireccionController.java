package com.programacionNCapas.SReynaProgramacionNCapas.RestController;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.DireccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dirección Controller")
@RestController
@RequestMapping("/api/direccion")
public class DireccionController {

   
    
    @Autowired
    private DireccionService direccionService;

    /**
     * Obtiene una Dirección por su ID
     *
     * @param IdDireccion
     * @return
     */
    @Operation(summary = "Obtener dirección por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dirección encontrada"),
        @ApiResponse(responseCode = "400", description = "Dirección no encontrada")
    })
    @GetMapping("/{IdDireccion}")
    public ResponseEntity GetOne(@PathVariable int IdDireccion) {
        Result result = direccionService.GetById(IdDireccion);
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Agrega una nueva dirección
     *
     * @param usuario
     * @return
     */
    @Operation(summary = "Agregar una nueva dirección")
    @ApiResponse(responseCode = "200", description = "Dirección creada correctamente")
    @PostMapping()
    public ResponseEntity Add(@RequestBody UsuarioJPA usuario) {
        Result result = direccionService.Add(usuario);
        return ResponseEntity.status(result.status).body(result);

    }

    /**
     * Editar una dirección
     *
     * @param usuario
     * @param IdDireccion
     * @return
     */
    @Operation(summary = "Editar una dirección")
    @ApiResponse(responseCode = "200", description = "Dirección editada correctamente")
    @PutMapping("{IdDireccion}")
    public ResponseEntity Update(@PathVariable int IdDireccion,
            @RequestBody UsuarioJPA usuario) {
        usuario.Direcciones.get(0).setIdDireccion(IdDireccion);
        Result result = direccionService.Update(usuario);
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Elimina una dirección físicamente
     *
     * @param IdDireccion
     * @return
     */
    @Operation(summary = "Eliminar dirección físicamente", description = "Elimina un registro de dirección en la base de datos")
    // @CrossOrigin(origins = "http://localhost:8081")
    @DeleteMapping("/{IdDireccion}")
    public ResponseEntity Delete(@PathVariable() int IdDireccion) {
        Result result = direccionService.Delete(IdDireccion);
        return ResponseEntity.status(result.status).body(result);
    }

}
