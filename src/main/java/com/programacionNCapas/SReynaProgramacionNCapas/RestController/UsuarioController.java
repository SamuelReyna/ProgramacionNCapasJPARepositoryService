package com.programacionNCapas.SReynaProgramacionNCapas.RestController;

import ch.qos.logback.core.model.Model;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.ColoniaJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.DireccionJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.ErrorCM;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Key;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.RolJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Usuario Controller", description = "Operaciones CRUD para la gestión de usuarios")
@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene todos los usuarios de la base de datos
     *
     * @return
     */
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve la lista completa de usuarios")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity GetAll() {
        Result result = usuarioService.GetAll();
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Obtiene un usuario por su ID
     *
     * @param IdUsuario
     * @return
     */
    @Operation(summary = "Obtener un usuario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "400", description = "Usuario no encontrado")
    })
    @GetMapping("/{IdUsuario}")
    public ResponseEntity GetOne(
            @Parameter(description = "ID del usuario a buscar")
            @PathVariable int IdUsuario) {
        Result result = usuarioService.GetById(IdUsuario);
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Agrega un nuevo usuario
     *
     * @param usuario
     * @return
     */
    @Operation(summary = "Agregar un nuevo usuario")
    @ApiResponse(responseCode = "200", description = "Usuario creado correctamente")
    @PostMapping()
    public ResponseEntity Add(
            @Parameter(description = "Usuario con los datos necesarios")
            @RequestBody UsuarioJPA usuario) {
        Result result = usuarioService.Add(usuario);
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Actualiza un usuario existente
     *
     * @param IdUsuario
     * @param usuario
     * @return
     */
    @Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente")
    @PutMapping("/{IdUsuario}")
    public ResponseEntity Update(
            @Parameter(description = "ID del usuario a actualizar")
            @PathVariable int IdUsuario,
            @RequestBody UsuarioJPA usuario) {
        usuario.setIdUser(IdUsuario);
        Result result = usuarioService.Update(usuario);
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Elimina un usuario físicamente
     *
     * @param IdUsuario
     * @return
     */
    @Operation(summary = "Eliminar usuario físicamente", description = "Elimina un registro de usuario en la base de datos")
    @DeleteMapping("/{IdUsuario}")
    public ResponseEntity Delete(
            @Parameter(description = "ID del usuario a eliminar")
            @PathVariable int IdUsuario) {
        Result result = usuarioService.Delete(IdUsuario);
        return ResponseEntity.status(result.status).body(result);
    }

    /**
     * Desactiva un usuario (eliminación lógica)
     *
     * @param IdUsuario
     * @return
     */
    @Operation(summary = "Eliminar usuario lógicamente", description = "Cambia el estatus del usuario en lugar de borrarlo")
    @PatchMapping("/estatus/{IdUsuario}")
    public ResponseEntity LogicalDelete(
            @Parameter(description = "ID del usuario a desactivar")
            @PathVariable int IdUsuario) {
        Result result = usuarioService.LogicalDelete(IdUsuario);
        return ResponseEntity.status(result.status).body(result);
    }

//    @Operation(
//            summary = "Carga un archivo TXT o XLSX para validación",
//            description = "El archivo se guarda, se calcula un hash SHA-1 y se registra en logs. "
//            + "Si contiene errores se devuelven en la respuesta."
//    )
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Archivo cargado exitosamente"),
//        @ApiResponse(responseCode = "202", description = "Archivo con errores, ver detalles en respuesta"),
//        @ApiResponse(responseCode = "400", description = "Formato no soportado o archivo duplicado")
//    })
//    @PostMapping(value = "/cargamasiva", consumes = "multipart/form-data")
//    public ResponseEntity CargaMasiva(
//            @Parameter(
//                    description = "Archivo a cargar (txt o xlsx)",
//                    required = true,
//                    content = @Content(mediaType = "multipart/form-data")
//            )
//            @RequestParam("archivo") MultipartFile file) {
//        Result result = new Result();
//        List<UsuarioJPA> usuarios = new ArrayList<>();
//        List<ErrorCM> errores = new ArrayList<>();
//
//        // validar extensión
//        String extension = file.getOriginalFilename().split("\\.")[1];
//        boolean valid = extension.equals("txt") || extension.equals("xlsx");
//
//        if (!valid) {
//            errores.add(new ErrorCM(1, "", "Tipo de Archivo Invalido"));
//            result.correct = false;
//            result.errorMessage = "Formato no soportado. Solo se aceptan TXT y XLSX.";
//            return ResponseEntity.status(400).body(result);
//        }
//
//        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/files/files/";
//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        try {
//            // Generar hash SHA-1 del contenido del archivo
//            // Guardar el archivo
//            String upDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
//
//            File destino = new File(uploadDir + upDate + "_" + file.getOriginalFilename());
//
//            // usar la ruta completa como String
//            String rutaCompleta = destino.getAbsolutePath();
//
//            // calcular SHA-1 de la ruta
//            MessageDigest md = MessageDigest.getInstance("SHA-1");
//            byte[] messageDigest = md.digest(rutaCompleta.getBytes(StandardCharsets.UTF_8));
//            BigInteger no = new BigInteger(1, messageDigest);
//
//            // convertir a string hexadecimal de 40 caracteres
//            String pathHash = no.toString(16);
//            while (pathHash.length() < 40) {
//                pathHash = "0" + pathHash;
//            }
//
//            //crear archivo para logs.
//            String logUpload = System.getProperty("user.dir") + "/src/main/resources/files/logs/";
//            File logDir = new File(logUpload);
//            if (!logDir.exists()) {
//                logDir.mkdirs();
//            }
//            File logsCM = new File(logDir + "/logsCM.txt");
//
//            if (destino.exists()) {
//                result.correct = false;
//                result.errorMessage = "El archivo ya fue cargado anteriormente (mismo contenido).";
//                return ResponseEntity.status(400).body(result);
//            }
//
//            file.transferTo(destino);
//
//            // procesar archivo según extensión
//            if (extension.equalsIgnoreCase("txt")) {
//                usuarios = ProcesarTXT(destino);
//            } else if (extension.equalsIgnoreCase("xlsx")) {
//                usuarios = ProcesarExcel(destino);
//            }
//
//            // validaciones
//            errores = ValidarDatos(usuarios);
//
//            if (errores != null && !errores.isEmpty()) {
//                try (FileWriter fw = new FileWriter(logsCM, true); PrintWriter writer = new PrintWriter(fw)) {
//                    String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                    writer.println("log|" + pathHash + "|" + upDate + "_" + file.getOriginalFilename() + "|" + status.ERROR.ordinal() + "|" + timeStamp + "|" + "Archvo con errores");
//                    result.object = errores;
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                return ResponseEntity.status(202).body(result);
//            } else {
//                try (FileWriter fw = new FileWriter(logsCM, true); PrintWriter writer = new PrintWriter(fw)) {
//                    String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                    writer.println("log|" + pathHash + "|" + upDate + "_" + file.getOriginalFilename() + "|" + status.PROCESAR.ordinal() + "|" + timeStamp + "|" + "Archivo listo para procesar");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//            result.correct = true;
//            result.object = pathHash;
//
//        } catch (Exception e) {
//            result.correct = false;
//            result.errorMessage = "Error al procesar archivo: " + e.getMessage();
//        }
//
//        return ResponseEntity.status(200).body(result);
//    }
//
//    @Operation(
//            summary = "Procesa un archivo previamente cargado",
//            description = "Busca en el log por el hash (key). Si el archivo está listo y dentro del tiempo permitido, lo procesa."
//    )
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Archivo procesado correctamente"),
//        @ApiResponse(responseCode = "400", description = "Error en el procesamiento o tiempo expirado")
//    })
//    @PostMapping(value = "/cargamasiva/procesar", consumes = "application/json")
//    public ResponseEntity Procesar(
//            @RequestBody() Key key) {
//        String logUpload = System.getProperty("user.dir") + "/src/main/resources/files/logs/";
//        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/files/files/";
//        Result result = new Result();
//        String llave = key.getKey();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(logUpload + "logsCM.txt"))) {
//            String linea;
//            while ((linea = br.readLine()) != null) {
//                String id = linea.split("\\|")[1];
//
//                if (llave.equals(id)) {
//                    int statusLog = Integer.parseInt(linea.split("\\|")[3]);
//                    if (statusLog == 0) {
//
//                        String date = linea.split("\\|")[4].trim();
//                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                        LocalDateTime fechaLog = LocalDateTime.parse(date, formato);
//                        LocalDateTime nowDateTime = LocalDateTime.now();
//
//                        long minutes = ChronoUnit.MINUTES.between(fechaLog, nowDateTime);
//                        String file = linea.split("\\|")[2];
//                        if (minutes <= 2) {
//
//                            String path = uploadDir + file;
//                            List<UsuarioJPA> usuarios;
//                            if (file.split("\\.")[1].equals("txt")) {
//                                usuarios = ProcesarTXT(new File(path));
//                            } else {
//                                usuarios = ProcesarExcel(new File(path));
//                            }
//
//                            try (FileWriter fw = new FileWriter(logUpload + "/logsCM.txt", true); PrintWriter writer = new PrintWriter(fw)) {
//                                String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                                writer.println("log|" + llave + "|" + file + "|" + status.PROCESADO.ordinal() + "|" + timeStamp + "|Archivo procesado");
//
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//
//                            for (UsuarioJPA usuario : usuarios) {
//                                usuarioDAOJPAImplementation.Add(usuario);
//                            }
//                            result.errorMessage = null;
//                            result.correct = true;
//                            result.status = 200;
//                        } else {
//                            result.correct = false;
//                            result.status = 400;
//                            result.errorMessage = "Tiempo agotado";
//                            try (FileWriter fw = new FileWriter(logUpload + "/logsCM.txt", true); PrintWriter writer = new PrintWriter(fw)) {
//                                String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                                writer.println("log|" + llave + "|" + file + "|" + status.ERROR.ordinal() + "|" + timeStamp + "|Tiempo de procesado expirado");
//
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            result.correct = false;
//            result.status = 400;
//            result.errorMessage = ex.getLocalizedMessage();
//            result.ex = ex;
//        }
//
//        return ResponseEntity.status(result.status).body(result);
//    }

    private List<UsuarioJPA> ProcesarTXT(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            String linea = "";
            List<UsuarioJPA> usuarios = new ArrayList<>();
            while ((linea = bufferedReader.readLine()) != null) {
                String[] campos = linea.split("\\|");
                UsuarioJPA usuario = new UsuarioJPA();
                usuario.Rol = new RolJPA();
                usuario.Direcciones = new ArrayList<>();
                DireccionJPA direccion = new DireccionJPA();
                direccion.Colonia = new ColoniaJPA();

                usuario.setNombreUsuario(campos[0]);
                usuario.setApellidoPaterno(campos[1]);
                usuario.setApellidoMaterno(campos[2]);
                usuario.setFechaNacimiento(LocalDate.parse(campos[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                usuario.setPassword(campos[4]);
                usuario.setSexo(campos[5]);
                usuario.setUsername(campos[6]);
                usuario.setEmail(campos[7]);
                usuario.setTelefono(campos[8]);
                usuario.setCelular(campos[9]);
                usuario.setCurp(campos[10]);
                usuario.Rol.setIdRol(Integer.parseInt(campos[11]));
                direccion.setCalle(campos[12]);
                direccion.setNumeroInterior(campos[13]);
                direccion.setNumeroExterior(campos[14]);
                direccion.Colonia.setIdColonia(Integer.parseInt(campos[15]));
                usuario.setImg("");
                usuario.Direcciones.add(direccion);
                direccion.setUsuario(usuario);
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (Exception ex) {
            System.out.println(ex);
            return new ArrayList<>();
        }

    }

    private List<UsuarioJPA> ProcesarExcel(File file) {
        List<UsuarioJPA> usuarios = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                UsuarioJPA usuario = new UsuarioJPA();
                usuario.setNombreUsuario(row.getCell(0) != null ? row.getCell(0).toString() : "");
                usuario.setApellidoPaterno(row.getCell(1) != null ? row.getCell(1).toString() : "");
                usuario.setApellidoMaterno(row.getCell(2) != null ? row.getCell(2).toString() : "");
                usuario.setFechaNacimiento(
                        row.getCell(3).getCellType() == CellType.NUMERIC
                        ? row.getCell(3).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : LocalDate.parse(row.getCell(3).getStringCellValue(), DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                );
                usuario.setPassword(row.getCell(4).toString());
                usuario.setSexo(row.getCell(5).toString());
                usuario.setUsername(row.getCell(6).toString());
                usuario.setEmail(row.getCell(7).toString());
                usuario.setTelefono(formatter.formatCellValue(row.getCell(8)));
                usuario.setCelular(formatter.formatCellValue(row.getCell(9)));
                usuario.setCurp(row.getCell(10).toString());
                usuario.Rol = new RolJPA();
                usuario.Rol.setIdRol((int) row.getCell(11).getNumericCellValue());
                usuario.Direcciones = new ArrayList<>();
                DireccionJPA direccion = new DireccionJPA();
                direccion.Colonia = new ColoniaJPA();
                direccion.setCalle(row.getCell(12).toString());
                direccion.setNumeroInterior(formatter.formatCellValue(row.getCell(13)));
                direccion.setNumeroExterior(formatter.formatCellValue(row.getCell(14)));
                direccion.Colonia.setIdColonia((int) row.getCell(15).getNumericCellValue());
                usuario.Direcciones.add(direccion);

                usuarios.add(usuario);
            }

            return usuarios;

        } catch (Exception ex) {
            System.out.println(ex);
            return new ArrayList<>();
        }

    }

    private List<ErrorCM> ValidarDatos(List<UsuarioJPA> usuarios) {
        List<ErrorCM> errores = new ArrayList<>();
        int linea = 1;

        for (UsuarioJPA usuario : usuarios) {
            if (usuario.getNombreUsuario() == null
                    || "".equals(usuario.getNombreUsuario())) {
                errores.add(new ErrorCM(linea, "".equals(usuario.getNombreUsuario()) ? "vacio" : usuario.getNombreUsuario(), "Nombre es un campo obligatorio"));
            } else if (!OnlyLetters(usuario.getNombreUsuario())) {
                errores.add(new ErrorCM(linea, usuario.getNombreUsuario(), "Nombre no cumple con el formato requerido"));
            }
            if (usuario.getApellidoPaterno() == null
                    || "".equals(usuario.getApellidoPaterno())) {
                errores.add(new ErrorCM(linea, "".equals(usuario.getApellidoPaterno()) ? "vacio" : usuario.getApellidoPaterno(), "Apellido Paterno es un campo obligatorio"));
            } else if (!OnlyLetters(usuario.getApellidoPaterno())) {
                errores.add(new ErrorCM(linea, usuario.getApellidoPaterno(), "Apellido Paterno no cumple con el formato requerido"));
            }
            if (usuario.getApellidoMaterno() == null
                    || "".equals(usuario.getApellidoMaterno())) {
                errores.add(new ErrorCM(linea, "".equals(usuario.getApellidoMaterno()) ? "vacio" : usuario.getApellidoMaterno(), "Apellido Materno es un campo obligatorio"));
            } else if (!OnlyLetters(usuario.getApellidoMaterno())) {
                errores.add(new ErrorCM(linea, usuario.getApellidoMaterno(), "Apellido Materno no cumple con el formato requerido"));
            }
            if (usuario.getPassword() == null
                    || "".equals(usuario.getPassword())) {
                errores.add(new ErrorCM(linea, "".equals(usuario.getPassword()) ? "vacio" : "Vacio", "Contraseña es un campo obligatorio"));
            } else if (!validatePassword(usuario.getPassword())) {
                errores.add(new ErrorCM(linea, usuario.getPassword(), "Contrasena no cumple con el formato requerido"));
            }
            if (usuario.getEmail() == null
                    || "".equals(usuario.getEmail())) {
                errores.add(new ErrorCM(linea, "".equals(usuario.getEmail()) ? "vacio" : usuario.getEmail(), "Email es un campo obligatorio"));
            } else if (!validateEmail(usuario.getEmail())) {
                errores.add(new ErrorCM(linea, usuario.getEmail(), "Email no cumple con el formato requerido"));
            }
            if (usuario.getUsername() == null
                    || "".equals(usuario.getUsername())) {
                errores.add(new ErrorCM(linea, "".equals(usuario.getUsername()) ? "vacio" : usuario.getUsername(), "Username es un campo obligatorio"));
            } else if (!validateUsername(usuario.getUsername())) {
                errores.add(new ErrorCM(linea, usuario.getUsername(), "Username no cumple con el formato requerido"));
            }

            linea++;
        }
        return errores;
    }

    static boolean OnlyLetters(String text) {
        String regexOnlyLetters = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$";
        Pattern pattern = Pattern.compile(regexOnlyLetters);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    static boolean validatePassword(String text) {
        String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regexPassword);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    static boolean validateEmail(String text) {
        String regexEmail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    static boolean validateUsername(String text) {
        String regexUsername = "^(?!.*[_.]{2})[a-zA-Z0-9](?!.*[_.]{2})[a-zA-Z0-9._]{1,14}[a-zA-Z0-9]$";
        Pattern pattern = Pattern.compile(regexUsername);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public enum status {
        PROCESAR, ERROR, PROCESADO
    }
}
