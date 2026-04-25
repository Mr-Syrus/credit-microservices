package com.mr_syrus.credit.api.service;

import com.mr_syrus.credit.api.dto.CodeVerificationDto;
import com.mr_syrus.credit.api.dto.CreateApplicationDto;
import com.mr_syrus.credit.api.dto.RegistrationClientDto;
import com.mr_syrus.credit.api.entity.*;
import com.mr_syrus.credit.api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Transactional
    public Integer createApplication(CreateApplicationDto dto, UserEntity currentUser) {
        // 1. Получить персональные данные пользователя
        PersonalDataEntity personalData = personalDataRepository.findByUser(currentUser)
                .orElseThrow(() -> new IllegalStateException("Personal data not found"));

        // 2. Проверить соответствие верификационных данных
        if (!normalize(personalData.getPassportSeries()).equals(normalize(dto.getPassportSeries())) ||
                !normalize(personalData.getPassportNumber()).equals(normalize(dto.getPassportNumber())) ||
                !normalize(personalData.getInn()).equals(normalize(dto.getInn())) ||
                !normalize(personalData.getSnils()).equals(normalize(dto.getSnils())) ||
                !personalData.getBirthDate().equals(dto.getBirthDate())) {
            throw new IllegalArgumentException("Verification data does not match stored personal data");
        }

        // 2.1 Проверка по спискам Росфинмониторинга (заглушка)
        // В реальном проекте здесь должен быть вызов внешнего API Росфинмониторинга
        // Передать паспортные данные, ИНН, СНИЛС, ФИО
        // Если API вернёт статус "IN_LIST_FULL_BLOCK" - выбросить исключение
        // Пока просто запрос к заранее заданному полю
        if (personalData.getRosfinmonitoringStatus() != RosfinmonitoringStatus.NOT_RESTRICTED) {
            throw new IllegalArgumentException("Client is blacklisted by Rosfinmonitoring");
        }

        // 2.2 Проверка кредитного продукта
        CreditEntity credit = creditRepository.findById(dto.getCreditId())
                .orElseThrow(() -> new IllegalArgumentException("Credit product not found"));

        // 2.3 Проверка допустимости суммы и срока
        if (dto.getCreditAmount().compareTo(credit.getMinAmount()) < 0 ||
                dto.getCreditAmount().compareTo(credit.getMaxAmount()) > 0) {
            throw new IllegalArgumentException("Credit amount out of allowed range");
        }
        if (dto.getCreditTerm() < credit.getMinTermMonths() ||
                dto.getCreditTerm() > credit.getMaxTermMonths()) {
            throw new IllegalArgumentException("Credit term out of allowed range");
        }

        // 2.4 Создать заявку
        ApplicationEntity application = new ApplicationEntity(personalData, credit, dto.getCreditTerm(), dto.getCreditAmount());
        application = applicationRepository.save(application);

        return application.getId();
    }

    private String normalize(String value) {
        return value == null ? null : value.replaceAll("[-\\s]", "");
    }

}
