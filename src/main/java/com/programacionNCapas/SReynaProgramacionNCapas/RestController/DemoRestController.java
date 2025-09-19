//package com.programacionNCapas.SReynaProgramacionNCapas.RestController;
//
//import com.programacionNCapas.SReynaProgramacionNCapas.DAOJPA.UsuarioDAOJPAImplementation;
//import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.IntStream;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("demo")
//public class DemoRestController {
//
//    @Autowired
//    private UsuarioDAOJPAImplementation usuarioDAOJPAImplementation;
//
//    @GetMapping("/suma")
//    public String Suma(@RequestParam int numA, int numB) {
//        return "Suma: " + (numA + numB);
//    }
//
//    @GetMapping("/saludo")
//    public ResponseEntity<Map<String, Object>> Saludo(@RequestParam int IdUsuario) {
//        UsuarioJPA usuario = (UsuarioJPA) usuarioDAOJPAImplementation.GetOne(IdUsuario).object;
//        if (usuario != null) {
//            String nombre = usuario.getNombreUsuario() + " " + usuario.getApellidoMaterno() + " " + usuario.getApellidoMaterno();
//            String saludo = "Hola, " + nombre;
//            Map<String, Object> response = new HashMap<>();
//            response.put("Mensaje", saludo);
//            response.put("Usuario", usuario);
//            return ResponseEntity.ok(response);
//
//        } else {
//            Map<String, Object> response = new HashMap<>();
//            String mensaje = "Usuario no Encontrado";
//            response.put(mensaje, null);
//            return ResponseEntity.status(404).body(response);
//        }
//    }
//
//    @PostMapping("/suma")
//    public String SumaArray(@RequestBody int[] numbers) {
//        int result = IntStream.of(numbers).sum();
//        return "El resultado de la suma es: " + result;
//    }
//    
//    @PatchMapping("/patchArray")
//    public String[] PatchArray(
//            @RequestParam int Index, String name,
//            @RequestBody String[] Nombres
//    ){
//        Nombres[Index] = name;
//        
//        return Nombres;
//    }
//    
//
//
//    
//}
