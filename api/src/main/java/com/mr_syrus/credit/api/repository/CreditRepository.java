package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<CreditEntity, Integer> {
}
