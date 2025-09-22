package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.ColoniaJPA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IColoniaRepository extends JpaRepository<ColoniaJPA, Integer> {
    
    List<ColoniaJPA> findByMunicipio_IdMunicipio(int idMunicipio);

}
