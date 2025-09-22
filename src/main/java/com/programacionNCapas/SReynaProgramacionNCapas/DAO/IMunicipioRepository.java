
package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.MunicipioJPA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMunicipioRepository extends JpaRepository<MunicipioJPA, Integer> {
    
    List<MunicipioJPA> findByEstado_IdEstado(int idEstado);
}
