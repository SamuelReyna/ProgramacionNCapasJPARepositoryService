package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IRolRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    @Autowired
    private IRolRepository iRolRepository;

    public Result GetAll() {
        Result result = new Result();
        try {
            result.object = iRolRepository.findAll();
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
