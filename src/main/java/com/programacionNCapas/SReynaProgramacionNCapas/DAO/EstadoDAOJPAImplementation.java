package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.EstadoJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EstadoDAOJPAImplementation implements IEstadoDAOJPA {

    @Autowired
    EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result result = new Result();

        try {
            TypedQuery<EstadoJPA> queryEstado
                    = entityManager.createQuery("FROM estado", EstadoJPA.class);
            List<EstadoJPA> estados = queryEstado.getResultList();
            result.objects = new ArrayList<>();
            for (EstadoJPA estado : estados) {
                result.objects.add(estado);
            }
            result.status = 200;
            result.correct = true;
        } catch (Exception ex) {
            result.ex = ex;
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
            result.status = 500;

        }
        return result;
    }

    @Override
    public Result GetByIdPais(int idPais) {
        Result result = new Result();

        try {
            TypedQuery<EstadoJPA> queryEstado
                    = entityManager.createQuery("FROM estado e WHERE e.Pais.IdPais = :idpais", EstadoJPA.class);
            queryEstado.setParameter("idpais", idPais);
            List<EstadoJPA> estados = queryEstado.getResultList();
            result.objects = new ArrayList<>();
            for (EstadoJPA estado : estados) {
                result.objects.add(estado);
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
