package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IEstadoRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {

    @Autowired
    private IEstadoRepository iEstadoRepository;

    public Result GetAll() {
        Result result = new Result();
        try {
            result.object = iEstadoRepository.findAll();
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;

        }
        return result;
    }

    public Result GetByPais(int idPais) {
        Result result = new Result();
        try {
            result.object = iEstadoRepository.findByPais_IdPais(idPais);
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }
}
