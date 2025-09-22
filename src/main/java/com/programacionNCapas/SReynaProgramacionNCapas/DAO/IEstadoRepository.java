
package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.EstadoJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoRepository extends JpaRepository<EstadoJPA, Integer> {
    
    EstadoJPA findByPais_IdPais(int idPais);
}
