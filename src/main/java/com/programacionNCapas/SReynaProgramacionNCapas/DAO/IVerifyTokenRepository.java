package com.programacionNCapas.SReynaProgramacionNCapas.DAO;

import com.programacionNCapas.SReynaProgramacionNCapas.JPA.VerifyToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVerifyTokenRepository extends JpaRepository<VerifyToken, Long> {

    void deleteByUserId(int userId);

    Optional<VerifyToken> findByToken(String token);

}
