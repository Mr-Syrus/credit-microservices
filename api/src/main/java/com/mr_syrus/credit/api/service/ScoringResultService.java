package com.mr_syrus.credit.api.service;

import com.mr_syrus.credit.api.dto.ScoringResponseDto;
import com.mr_syrus.credit.api.entity.ApplicationEntity;
import com.mr_syrus.credit.api.entity.ApplicationStatus;
import com.mr_syrus.credit.api.entity.ScoringResultEntity;
import com.mr_syrus.credit.api.repository.ApplicationRepository;
import com.mr_syrus.credit.api.repository.ScoringResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScoringResultService {

    private final ScoringResultRepository resultRepository;
    private final ApplicationRepository applicationRepository;

    public ScoringResultService(ScoringResultRepository resultRepository,
                                ApplicationRepository applicationRepository) {
        this.resultRepository = resultRepository;
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public void saveScoringResult(ScoringResponseDto dto) {
        ApplicationEntity application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + dto.getApplicationId()));

        ScoringResultEntity entity = new ScoringResultEntity();
        entity.setApplication(application);
        entity.setProbability(dto.getProbability());
        entity.setDecision(dto.getDecision());
        entity.setError(dto.getError());
        entity.setCreatedAt(LocalDateTime.now());

        resultRepository.save(entity);

        // обновить статус заявки
        if ("approve".equals(dto.getDecision())) {
            application.setStatus(ApplicationStatus.APPROVED);
        } else if ("reject".equals(dto.getDecision())) {
            application.setStatus(ApplicationStatus.REJECTED);
        }
        applicationRepository.save(application);
    }
}