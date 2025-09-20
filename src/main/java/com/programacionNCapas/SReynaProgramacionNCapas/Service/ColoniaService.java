package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IColoniaRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColoniaService {

    @Autowired
    private IColoniaRepository iColoniaRepository;

    public Result GetByMunicipio(int idMunicipio) {
        Result result = new Result();

        try {
            result.object = iColoniaRepository.findByIdMunicipio_IdMunicipio(idMunicipio);
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
