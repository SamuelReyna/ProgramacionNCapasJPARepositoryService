package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IColoniaRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IDireccionRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IUsuarioRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.ColoniaJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.DireccionJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DireccionService {

    @Autowired
    private IDireccionRepository iDireccionRepository;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private IColoniaRepository iColoniaRepository;

    public Result GetAll() {
        Result result = new Result();

        try {
            result.object = iDireccionRepository.findAll();
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

    public Result GetById(int idDireccion) {
        Result result = new Result();

        try {
            Optional<DireccionJPA> direccion = iDireccionRepository.findById(idDireccion);
            if (direccion.isPresent()) {
                result.object = direccion.get();
                result.correct = true;
                result.status = 200;
            } else {
                result.correct = false;
                result.errorMessage = "Dirección no encontrada";
                result.status = 404;
            }

        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }

    public Result Add(UsuarioJPA usuario) {
        Result result = new Result();

        try {


            DireccionJPA direccion = usuario.Direcciones.get(0);

            UsuarioJPA usuarioRef = iUsuarioRepository.getReferenceById(usuario.getIdUser());
            ColoniaJPA coloniaRef = iColoniaRepository.getReferenceById(direccion.Colonia.getIdColonia());

            DireccionJPA direccionJPA = new DireccionJPA();

            direccionJPA.setCalle(direccion.getCalle());
            direccionJPA.setNumeroExterior(direccion.getNumeroExterior());
            direccionJPA.setNumeroInterior(direccion.getNumeroInterior());
            direccionJPA.setColonia(coloniaRef);
            direccionJPA.setUsuario(usuarioRef);

            iDireccionRepository.save(direccionJPA);

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

    public Result Update(UsuarioJPA usuario) {
        Result result = new Result();

        try {
            Optional<DireccionJPA> direccionBD = iDireccionRepository.findById(usuario.Direcciones.get(0).getIdDireccion());
            if (direccionBD.isPresent()) {
                DireccionJPA direccion = usuario.Direcciones.get(0);

                UsuarioJPA usuarioRef = iUsuarioRepository.getReferenceById(usuario.getIdUser());
                ColoniaJPA coloniaRef = iColoniaRepository.getReferenceById(usuario.Direcciones.get(0).Colonia.getIdColonia());
                DireccionJPA direccionEdit = direccionBD.get();
                direccionEdit.setCalle(direccion.getCalle());
                direccionEdit.setNumeroInterior(direccion.getNumeroInterior());
                direccionEdit.setNumeroExterior(direccion.getNumeroExterior());
                direccionEdit.setUsuario(usuarioRef);
                direccionEdit.setColonia(coloniaRef);

                iDireccionRepository.save(direccionEdit);
                result.correct = true;
                result.status = 200;
            } else {
                result.correct = false;
                result.status = 404;
                result.errorMessage = "No existe";

            }

        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }

    public Result Delete(int IdDireccion) {
        Result result = new Result();
        try {
            iDireccionRepository.deleteById(IdDireccion);
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
