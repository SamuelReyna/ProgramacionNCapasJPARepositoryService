package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.MunicipioJPA;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioDAOJPAImplementation implements IMunicipioDAOJPA {

    @Autowired
    EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result result = new Result();

        try {
            TypedQuery<MunicipioJPA> queryMunicipio
                    = entityManager.createQuery("FROM municipio", MunicipioJPA.class);
            List<MunicipioJPA> municipios = queryMunicipio.getResultList();
            result.objects = new ArrayList<>();
            for (MunicipioJPA municipio : municipios) {
                result.objects.add(municipio);
            }
            result.correct = true;
            result.status = 200;

        } catch (Exception ex) {
            result.status = 500;
            result.ex = ex;
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
        }
        return result;
    }

    @Override
    public Result GetByIdEstado(int IdEstado) {
        Result result = new Result();

        try {
            TypedQuery<MunicipioJPA> queryMunicipio
                    = entityManager.createQuery("FROM municipio m WHERE m.Estado.IdEstado = :idestado", MunicipioJPA.class);
            queryMunicipio.setParameter("idestado", IdEstado);
            List<MunicipioJPA> municipios = queryMunicipio.getResultList();
            result.objects = new ArrayList<>();
            for (MunicipioJPA municipio : municipios) {
                result.objects.add(municipio);
            }
            result.correct = true;
            result.status = 200;
        } catch (Exception ex) {
            result.ex = ex;
            result.errorMessage = ex.getLocalizedMessage();
            result.correct = false;
        }
        return result;
    }

}
