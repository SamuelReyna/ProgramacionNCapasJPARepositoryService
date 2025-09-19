package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;

public interface IEstadoDAOJPA {

    Result GetAll();

    Result GetByIdPais(int idPais);

}
