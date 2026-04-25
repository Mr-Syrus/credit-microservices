package com.mr_syrus.credit.api.service;

import com.mr_syrus.credit.api.entity.AuthorizationCodeEntity;
import com.mr_syrus.credit.api.entity.PersonalDataEntity;
import com.mr_syrus.credit.api.entity.SessionEntity;
import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.repository.AuthorizationCodeRepository;
import com.mr_syrus.credit.api.repository.PersonalDataRepository;
import com.mr_syrus.credit.api.repository.SessionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final MailVerificationService mailVerificationService;
    private final AuthorizationCodeRepository authorizationCodeRepository;
    private final SessionRepository sessionRepository;
    private final PersonalDataRepository personalDataRepository;

    public AuthService(MailVerificationService mailVerificationService,
                       AuthorizationCodeRepository authorizationCodeRepository,
                       SessionRepository sessionRepository,
                       PersonalDataRepository personalDataRepository) {
        this.mailVerificationService = mailVerificationService;
        this.authorizationCodeRepository = authorizationCodeRepository;
        this.sessionRepository = sessionRepository;
        this.personalDataRepository = personalDataRepository;
    }

    @Transactional
    public String sendCode(String mail, String passportSeries, String passportNumber) {
        // Ищем активную запись паспортных данных
        PersonalDataEntity personalData = personalDataRepository
                .findActiveByMailAndPassportData(mail, passportSeries, passportNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Passport data not found or not active"));

        // Генерация и сохранение кода
        String code = mailVerificationService.sendVerificationCode(mail);
        AuthorizationCodeEntity authCode = new AuthorizationCodeEntity(code, personalData.getUser());
        authorizationCodeRepository.save(authCode);

        return authCode.getId().toString();
    }

    @Transactional
    public SessionEntity authenticate(String codeIdStr, String code, String ipAddress, String userAgent) {
        UUID codeId;
        try {
            codeId = UUID.fromString(codeIdStr);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid codeId");
        }

        AuthorizationCodeEntity authCode = authorizationCodeRepository.findById(codeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code not found"));

        // Проверка кода
        if (!authCode.getCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid code");
        }

        // Проверка срока действия
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(authCode.getDateTimeStart()) || now.isAfter(authCode.getDateTimeEnd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code expired");
        }

        // Удаляем использованный код
        authorizationCodeRepository.delete(authCode);

        // Создаём сессию
        SessionEntity session = new SessionEntity(authCode.getUser(), ipAddress, userAgent);
        sessionRepository.save(session);

        return session;
    }
}