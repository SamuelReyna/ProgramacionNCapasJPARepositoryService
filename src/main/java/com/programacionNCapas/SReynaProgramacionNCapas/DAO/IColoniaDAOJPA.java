package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.Result;

public interface IColoniaDAOJPA {

    Result GetAll();

    Result GetByIdMunicipio(int IdMunicipio);
}
