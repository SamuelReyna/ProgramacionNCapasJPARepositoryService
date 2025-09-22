
package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.EstadoJPA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoRepository extends JpaRepository<EstadoJPA, Integer> {
    
    List<EstadoJPA> findByPais_IdPais(int idPais);
}
