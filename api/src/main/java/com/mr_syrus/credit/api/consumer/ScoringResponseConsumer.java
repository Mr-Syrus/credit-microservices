package com.mr_syrus.credit.api.consumer;

import com.mr_syrus.credit.api.dto.ScoringResponseDto;
import com.mr_syrus.credit.api.service.ScoringResultService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ScoringResponseConsumer {
    private final ScoringResultService scoringResultService;
    public ScoringResponseConsumer(ScoringResultService scoringResultService) {
        this.scoringResultService = scoringResultService;
    }
    @KafkaListener(topics = "credit.responses", groupId = "java-scoring-group")
    public void listen(ScoringResponseDto response) {
        scoringResultService.saveScoringResult(response);
    }
}
