package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IUsuarioRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    public Result GetAll() {
        Result result = new Result();

        try {
            result.object = iUsuarioRepository.findAll();
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
            iUsuarioRepository.save(usuario);
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

    public Result Update(UsuarioJPA usuario) {
        Result result = new Result();
        try {
            Optional<UsuarioJPA> usuario1 = iUsuarioRepository.findById(usuario.getIdUser());
            if (usuario1.isPresent()) {
                iUsuarioRepository.save(usuario);
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

}
