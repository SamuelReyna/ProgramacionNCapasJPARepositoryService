package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IUsuarioRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Result GetAll(int pagina, int cantidad, String campo, String orden, String busqueda) {
        Result result = new Result();
        try {
            List<UsuarioJPA> usuarios = iUsuarioRepository.findAll();

            if (busqueda != null && !busqueda.trim().isEmpty()) {
                String busquedaLower = busqueda.toLowerCase();
                usuarios = usuarios.stream()
                        .filter(u
                                -> (u.getNombreUsuario() != null && u.getNombreUsuario().toLowerCase().contains(busquedaLower))
                        || (u.getApellidoPaterno() != null && u.getApellidoPaterno().toLowerCase().contains(busquedaLower))
                        || (u.getApellidoMaterno() != null && u.getApellidoMaterno().toLowerCase().contains(busquedaLower))
                        || (u.getEmail() != null && u.getEmail().toLowerCase().contains(busquedaLower))
                        || (u.getUsername() != null && u.getUsername().toLowerCase().contains(busquedaLower))
                        || (u.getCurp() != null && u.getCurp().toLowerCase().contains(busquedaLower))
                        )
                        .collect(Collectors.toList());
            }

            if (campo != null && !campo.trim().isEmpty()) {
                Comparator<UsuarioJPA> comparator = null;

                switch (campo.toLowerCase()) {
                    case "nombre":
                        comparator = Comparator.comparing(UsuarioJPA::getNombreUsuario,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
                        break;
                    case "apellidoPaterno":
                        comparator = Comparator.comparing(UsuarioJPA::getApellidoPaterno,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
                        break;
                    case "email":
                        comparator = Comparator.comparing(UsuarioJPA::getEmail,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
                        break;
                    case "usuario":
                        comparator = Comparator.comparing(UsuarioJPA::getUsername,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
                        break;

                    default:
                        comparator = Comparator.comparing(UsuarioJPA::getIdUser,
                                Comparator.nullsLast(Integer::compareTo));
                }

                if ("DESC".equalsIgnoreCase(orden)) {
                    comparator = comparator.reversed();
                }

                usuarios = usuarios.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }

            int total = usuarios.size();

            int inicio = (pagina - 1) * cantidad;
            int fin = Math.min(inicio + cantidad, total);

            if (inicio < total) {
                usuarios = usuarios.subList(inicio, fin);
            } else {
                usuarios = new ArrayList<>();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("data", usuarios);
            response.put("total", total);
            response.put("pagina", pagina);
            response.put("cantidad", cantidad);
            response.put("totalPaginas", (int) Math.ceil((double) total / cantidad));

            result.object = response;
            result.correct = true;
            result.status = 200;
        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.status = 500;
            result.ex = ex;
        }
        return result;
    }

    public Result GetById(int IdUsuario) {
        Result result = new Result();
        try {
            Optional<UsuarioJPA> usuario = iUsuarioRepository.findById(IdUsuario);
            if (usuario.isPresent()) {
                result.object = usuario.get();
                result.correct = true;
                result.status = 200;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrada";
                result.status = 404;
            }
        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.status = 500;

            result.ex = ex;
        }
        return result;
    }

    public Result Add(UsuarioJPA usuario) {
        Result result = new Result();
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            result.object = iUsuarioRepository.save(usuario);
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.status = 500;

            result.ex = ex;
        }
        return result;
    }

    @Transactional
    public Result Update(UsuarioJPA usuario) {
        Result result = new Result();
        try {
            Optional<UsuarioJPA> usuarioDBOpt = iUsuarioRepository.findById(usuario.getIdUser());
            if (usuarioDBOpt.isPresent()) {
                UsuarioJPA usuarioDB = usuarioDBOpt.get();

                if (usuario.getNombreUsuario() != null) {
                    usuarioDB.setNombreUsuario(usuario.getNombreUsuario());
                }

                if (usuario.getApellidoPaterno() != null) {
                    usuarioDB.setApellidoPaterno(usuario.getApellidoPaterno());
                }

                if (usuario.getApellidoMaterno() != null) {
                    usuarioDB.setApellidoMaterno(usuario.getApellidoMaterno());
                }

                if (usuario.getEmail() != null) {
                    usuarioDB.setEmail(usuario.getEmail());
                }

                if (usuario.getCelular() != null) {
                    usuarioDB.setCelular(usuario.getCelular());
                }

                if (usuario.getCurp() != null) {
                    usuarioDB.setCurp(usuario.getCurp());
                }

                if (usuario.getFechaNacimiento() != null) {
                    usuarioDB.setFechaNacimiento(usuario.getFechaNacimiento());
                }

                if (usuario.getImg() != null) {
                    usuarioDB.setImg(usuario.getImg());
                }

                if (usuario.getSexo() != null) {
                    usuarioDB.setSexo(usuario.getSexo());
                }

                if (usuario.getUsername() != null) {
                    usuarioDB.setUsername(usuario.getUsername());
                }

                if (usuario.getTelefono() != null) {
                    usuarioDB.setTelefono(usuario.getTelefono());
                }

                if (usuario.getPassword() != null) {
                    usuarioDB.setPassword(passwordEncoder.encode(usuario.getPassword()));
                }

                iUsuarioRepository.save(usuarioDB);

                result.correct = true;
                result.status = 200;
            } else {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Usuario no encontrado.";
            }
        } catch (Exception ex) {
            result.correct = false;
            result.status = 500;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    public Result Delete(int idUsuario) {
        Result result = new Result();
        try {
            iUsuarioRepository.deleteById(idUsuario);
            result.correct = true;
            result.status = 200;
        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }

    public Result LogicalDelete(int idUsuario) {
        Result result = new Result();
        try {
            Optional<UsuarioJPA> usuario = iUsuarioRepository.findById(idUsuario);
            if (usuario.isPresent()) {
                UsuarioJPA u = usuario.get();
                u.setEstatus(u.getEstatus() == 1 ? 0 : 1);
                iUsuarioRepository.save(u);
                result.correct = true;
                result.status = 200;
            }

        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.ex = ex;
            result.status = 500;

        }
        return result;
    }

    public Result Verify(int idUsuario) {
        Result result = new Result();
        try {
            Optional<UsuarioJPA> usuario = iUsuarioRepository.findById(idUsuario);
            if (usuario.isPresent()) {
                UsuarioJPA u = usuario.get();
                u.setEstatus(1);
                u.setVerify(1);
                iUsuarioRepository.save(u);
                result.correct = true;
                result.status = 200;
            } else {    
                result.correct = false;
                result.status = 404;
                result.errorMessage = "Usuario no existe";
            }
        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }

    public Result GetByUsername(String Username) {
        Result result = new Result();
        try {
            result.object = iUsuarioRepository.findByUsername(Username);
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioJPA usuario = (UsuarioJPA) iUsuarioRepository.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        return (UserDetails) usuario;
    }

    public Result loadUserByEmail(String Email) {
        Result result = new Result();
        try {
            result.object = iUsuarioRepository.findByEmail(Email);
            result.correct = true;
            result.status = 200;
        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }

}
