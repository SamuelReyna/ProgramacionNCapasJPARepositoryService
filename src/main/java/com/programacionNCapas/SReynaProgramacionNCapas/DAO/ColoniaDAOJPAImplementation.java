package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.ColoniaJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ColoniaDAOJPAImplementation implements IColoniaDAOJPA {

    @Autowired
    EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result result = new Result();

        try {
            TypedQuery<ColoniaJPA> queryColonia
                    = entityManager.createQuery("FROM colonia", ColoniaJPA.class);
            List<ColoniaJPA> colonias = queryColonia.getResultList();
            result.objects = new ArrayList<>();
            for (ColoniaJPA colonia : colonias) {
                result.objects.add(colonia);
            }
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.ex = ex;
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.status = 500;

        }
        return result;
    }

    @Override
    public Result GetByIdMunicipio(int IdMunicipio) {
        Result result = new Result();
        try {
            TypedQuery<ColoniaJPA> queryColonia
                    = entityManager.createQuery("FROM colonia c WHERE c.Municipio.IdMunicipio = :idmunicipio", ColoniaJPA.class);
            queryColonia.setParameter("idmunicipio", IdMunicipio);
            List<ColoniaJPA> colonias = queryColonia.getResultList();
            result.objects = new ArrayList<>();
            for (ColoniaJPA colonia : colonias) {
                result.objects.add(colonia);
            }
            result.correct = true;
            result.status = 200;
        } catch (Exception ex) {
            result.ex = ex;
            result.status = 500;
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
        }
        return result;
    }

}
