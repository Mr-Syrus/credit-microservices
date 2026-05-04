package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.ScoringResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoringResultRepository extends JpaRepository<ScoringResultEntity, Integer> {
}
