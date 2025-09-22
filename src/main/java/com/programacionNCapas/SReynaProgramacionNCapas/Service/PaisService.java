package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IPaisRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaisService {

    @Autowired
    private IPaisRepository iPaisRepository;

    public Result GetAll() {
        Result result = new Result();
        try {
            result.correct = true;
            result.status = 200;
            result.object = iPaisRepository.findAll();

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
        }
        return result;
    }

}
