package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IMunicipioRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MunicipioService {

    @Autowired
    private IMunicipioRepository iMunicipioRepository;

    public Result GetAll() {
        Result result = new Result();
        try {
            result.object = iMunicipioRepository.findAll();
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

    public Result GetByEstado(int idEstado) {
        Result result = new Result();
        try {
            result.object = iMunicipioRepository.findByEstado_IdEstado(idEstado);
            result.status = 200;
            result.correct = true;

        } catch (Exception ex) {
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }
}
