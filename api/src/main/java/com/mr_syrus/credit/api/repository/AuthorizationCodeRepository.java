package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.AuthorizationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCodeEntity, UUID> {
}
