
package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<UsuarioJPA, Integer>{
    
    
    UsuarioJPA findByUsername(String Username);  
    
    UsuarioJPA findByEmail(String Email);
}
