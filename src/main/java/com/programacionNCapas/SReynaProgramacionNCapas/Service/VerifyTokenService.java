package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IVerifyTokenRepository;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.VerifyToken;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class VerifyTokenService {

    private final IVerifyTokenRepository iVerifyTokenRepository;

    public VerifyTokenService(IVerifyTokenRepository iVerifyTokenRepository) {
        this.iVerifyTokenRepository = iVerifyTokenRepository;
    }

    @Transactional
    public String GenerateToken(int userId) {
        SecureRandom secureRandom = new SecureRandom();

        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(3);

        VerifyToken resetToken = new VerifyToken();
        resetToken.setExpDate(expiration);
        resetToken.setToken(token);
        resetToken.setIduser(userId);

        iVerifyTokenRepository.save(resetToken);
        return token;

    }

    public boolean validarToken(String token) {
        return iVerifyTokenRepository.findByToken(token)
                .filter(t -> t.getExpDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public int getUserIdbyToken(String token) {
        return iVerifyTokenRepository.findByToken(token)
                .filter(t -> t.getExpDate().isAfter(LocalDateTime.now()))
                .map(VerifyToken::getIduser)
                .orElse(null);
    }

    public void eliminarToken(String token) {
        iVerifyTokenRepository.findByToken(token).ifPresent(iVerifyTokenRepository::delete);
    }

}
