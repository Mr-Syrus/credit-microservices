package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, String> {
}
