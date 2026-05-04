package com.mr_syrus.credit.api.service;

import com.mr_syrus.credit.api.dto.ApplicationResponseDto;
import com.mr_syrus.credit.api.dto.CreateApplicationDto;
import com.mr_syrus.credit.api.dto.ScoringEventDto;
import com.mr_syrus.credit.api.entity.*;
import com.mr_syrus.credit.api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final PersonalDataRepository personalDataRepository;
    private final CreditRepository creditRepository;
    private final ApplicationRepository applicationRepository;
    private final KafkaTemplate<String, ScoringEventDto> kafkaTemplate;

    public ApplicationService(PersonalDataRepository personalDataRepository,
                         CreditRepository creditRepository,
                         ApplicationRepository applicationRepository,
                         KafkaTemplate<String, ScoringEventDto> kafkaTemplate
    ) {
        this.personalDataRepository = personalDataRepository;
        this.creditRepository = creditRepository;
        this.applicationRepository = applicationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public List<ApplicationResponseDto> getApplicationsByPassport(String passportSeries, String passportNumber) {
        List<ApplicationEntity> applications = applicationRepository
                .findByPassportSeriesAndNumber(passportSeries, passportNumber);
        if (applications.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No applications found for given active passport");
        }

        return applications.stream()
                .map(app -> new ApplicationResponseDto(
                        app.getId(),
                        app.getStatus().name(),
                        app.getCreditTerm(),
                        app.getCreditAmount(),
                        app.getCreatedDateTime(),
                        app.getCompletionDateTime(),
                        app.getCredit().getName()
                )).collect(Collectors.toList());

    }

    @Transactional
    public Integer createApplication(CreateApplicationDto dto, UserEntity currentUser) {
        // 1. Получить персональные данные пользователя
        PersonalDataEntity personalData = personalDataRepository.findByUser(currentUser)
                .orElseThrow(() -> new IllegalStateException("Personal data not found"));

        // 2. Проверить соответствие верификационных данных
        if (personalData.getPassportSeries().equals(dto.getPassportSeries()) ||
                personalData.getPassportNumber().equals(dto.getPassportNumber()) ||
                personalData.getInn().equals(dto.getInn()) ||
                personalData.getSnils().equals(dto.getSnils()) ||
                personalData.getBirthDate().equals(dto.getBirthDate())) {
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
        if (dto.getCreditTermMonths() < credit.getMinTermMonths() ||
                dto.getCreditTermMonths() > credit.getMaxTermMonths()) {
            throw new IllegalArgumentException("Credit term out of allowed range");
        }

        // 2.4 Собрать дто для скоринга
        int age = Period.between(dto.getBirthDate(), LocalDate.now()).getYears();
        ScoringEventDto eventDto = new ScoringEventDto(
                age,
                dto.getMonthlyIncome(),
                dto.getCreditAmount(),
                dto.getMaritalStatus(),
                dto.getCreditTermMonths());

        // 2.5 Создать заявку
        ApplicationEntity application = new ApplicationEntity(personalData, credit, dto.getCreditTermMonths(), dto.getCreditAmount());
        // 4 Сохранить все
        application = applicationRepository.save(application);
        sendScoringEvent(eventDto, application.getId());

        return application.getId();
    }

    private void sendScoringEvent(ScoringEventDto event, Integer applicationId) {
        kafkaTemplate.send("scoring-requests", event); //стоит добавить логирование
    }



}
