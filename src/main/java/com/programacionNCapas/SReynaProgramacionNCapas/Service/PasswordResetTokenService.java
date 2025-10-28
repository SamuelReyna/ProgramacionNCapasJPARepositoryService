package com.programacionNCapas.SReynaProgramacionNCapas.Service;

import com.programacionNCapas.SReynaProgramacionNCapas.DAO.IPasswordResetToken;
import com.programacionNCapas.SReynaProgramacionNCapas.JPA.PasswordResetToken;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenService {

    private final IPasswordResetToken iPasswordResetToken;

    public PasswordResetTokenService(IPasswordResetToken iPasswordResetToken) {
        this.iPasswordResetToken = iPasswordResetToken;
    }

    @Transactional
    public String GenerarToken(int userId) {
        SecureRandom secureRandom = new SecureRandom();

        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(3);

        iPasswordResetToken.deleteByUserId(userId);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setExpDateTime(expiration);
        resetToken.setToken(token);
        resetToken.setUserId(userId);

        iPasswordResetToken.save(resetToken);
        return token;
    }

    public boolean validarToken(String token) {
        return iPasswordResetToken.findByToken(token)
                .filter(t -> t.getExpDateTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public int getUserIdbyToken(String token) {
        return iPasswordResetToken.findByToken(token)
                .filter(t -> t.getExpDateTime().isAfter(LocalDateTime.now()))
                .map(PasswordResetToken::getUserId)
                .orElse(null);
    }

    public void eliminarToken(String token) {
        iPasswordResetToken.findByToken(token).ifPresent(iPasswordResetToken::delete);
    }
}
