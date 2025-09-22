package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.RolJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRolRepository  extends JpaRepository<RolJPA, Integer>{
    
}
