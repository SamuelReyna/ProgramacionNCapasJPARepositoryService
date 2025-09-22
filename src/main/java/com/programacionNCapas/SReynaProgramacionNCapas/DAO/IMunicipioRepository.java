
package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.MunicipioJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMunicipioRepository extends JpaRepository<MunicipioJPA, Integer> {
    
    MunicipioJPA findByEstado_IdEstado(int idEstado);
}
