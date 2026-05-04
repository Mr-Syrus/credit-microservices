package com.mr_syrus.credit.api.service;

import com.mr_syrus.credit.api.dto.CodeVerificationDto;
import com.mr_syrus.credit.api.dto.CreateApplicationDto;
import com.mr_syrus.credit.api.dto.RegistrationClientDto;
import com.mr_syrus.credit.api.dto.ScoringEventDto;
import com.mr_syrus.credit.api.entity.*;
import com.mr_syrus.credit.api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Service
public class ClientService {
    private final UserRepository userRepository;
    private final PersonalDataRepository personalDataRepository;
    private final RegistrationRepository registrationRepository;
    private final AuthorizationCodeRepository codeRepository;
    private final MailVerificationService mailService;
    private final SimplePasswordEncoder passwordEncoder;
    private final CreditRepository creditRepository;
    private final ApplicationRepository applicationRepository;

    public ClientService(UserRepository userRepository,
                         PersonalDataRepository personalDataRepository,
                         RegistrationRepository registrationRepository,
                         AuthorizationCodeRepository codeRepository,
                         MailVerificationService mailService,
                         SimplePasswordEncoder passwordEncoder,
                         CreditRepository creditRepository,
                         ApplicationRepository applicationRepository
                         ) {
        this.userRepository = userRepository;
        this.personalDataRepository = personalDataRepository;
        this.registrationRepository = registrationRepository;
        this.codeRepository = codeRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.creditRepository = creditRepository;
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public String register(RegistrationClientDto dto) {
        // 1. проверка уникальности
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByMail(dto.getMail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        if (personalDataRepository.existsByPhone(dto.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone already exists");
        }
        if (personalDataRepository.existsActiveByPassport(dto.getPassportSeries(), dto.getPassportNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passport already registered");
        }

        Role role = Role.CLIENT;

        // 2. cоздание пользователя (неактивного до подтверждения)
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        UserEntity user = new UserEntity(
                dto.getUsername(),
                dto.getMail(),
                hashedPassword,
                false, // active = false до подтверждения
                role
        );
        user = userRepository.save(user);

        // 2.1 запись персональных данных
        PersonalDataEntity personalData = new PersonalDataEntity(
                user,
                RosfinmonitoringStatus.NOT_RESTRICTED,
                dto.getMaritalStatus(),
                dto.getPassportSeries(),
                dto.getPassportNumber(),
                dto.getPassportIssuedBy(),
                dto.getDepartmentCode(),
                dto.getPassportIssueDate(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getMiddleName(),
                GenderStatus.valueOf(dto.getGender().toUpperCase()),
                dto.getBirthDate(),
                dto.getInn(),
                dto.getSnils(),
                dto.getPhone()
        );
        personalData.setActive(true);
        personalData = personalDataRepository.save(personalData);

        // 2.2 запись прописки
        RegistrationStatus status = RegistrationStatus.valueOf(dto.getRegistrationType().toUpperCase());

        RegistrationEntity registration = new RegistrationEntity(
                personalData,
                dto.getRegistrationDate(),
                dto.getPostalIndex(),
                dto.getMigrationDepartment(),
                dto.getRegion(),
                dto.getDistrict(),
                dto.getCity(),
                dto.getStreet(),
                dto.getHouse(),
                dto.getFlat(),
                status
        );
        registration.setActive(true); // текущая, активная прописка
        registrationRepository.save(registration);

        //отправка кода подтверждения
        String code = mailService.sendVerificationCode(dto.getMail());
        AuthorizationCodeEntity authCode = new AuthorizationCodeEntity(code, user);
        codeRepository.save(authCode);

        return authCode.getId().toString();
    }

    @Transactional
    public void confirmRegistration(CodeVerificationDto dto) {
        UUID codeId;
        try {
            codeId = UUID.fromString(dto.getCodeId());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid codeId");
        }

        AuthorizationCodeEntity authCode = codeRepository.findById(codeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code not found"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(authCode.getDateTimeStart()) || now.isAfter(authCode.getDateTimeEnd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code expired");
        }

        if (!authCode.getCode().equals(dto.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid code");
        }


        UserEntity user = authCode.getUser();
        if (user.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already confirmed");
        }
        user.setActive(true);
        userRepository.save(user);

        codeRepository.delete(authCode);
    }
}
