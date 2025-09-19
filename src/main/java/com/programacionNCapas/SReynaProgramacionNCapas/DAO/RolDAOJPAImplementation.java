package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.RolJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RolDAOJPAImplementation implements IRolDAOJPA {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result result = new Result();
        try {
            TypedQuery<RolJPA> queryRol
                    = entityManager.createQuery("FROM rol", RolJPA.class);
            List<RolJPA> roles = queryRol.getResultList();

            result.objects = new ArrayList<>();
            for (RolJPA rol : roles) {
                result.objects.add(rol);
            }
            result.status = 200;
            result.correct = true;
        } catch (Exception ex) {
            result.ex = ex;
            result.status = 500;
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
        }
            return result;
    }

}
