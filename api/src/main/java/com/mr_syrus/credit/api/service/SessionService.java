package com.mr_syrus.credit.api.service;

import com.mr_syrus.credit.api.entity.SessionEntity;
import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public UserEntity getUserBySessionKey(String sessionKey) {
        SessionEntity session = sessionRepository.findById(sessionKey)
                .orElseThrow(() -> new IllegalArgumentException("Invalid session"));
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Session expired");
        }
        return session.getUser();
    }
}
