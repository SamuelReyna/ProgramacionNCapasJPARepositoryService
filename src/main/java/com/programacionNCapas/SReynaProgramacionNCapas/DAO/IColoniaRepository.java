package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.ColoniaJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IColoniaRepository extends JpaRepository<ColoniaJPA, Integer> {
    
    ColoniaJPA findByIdMunicipio_IdMunicipio(int idMunicipio);

}
