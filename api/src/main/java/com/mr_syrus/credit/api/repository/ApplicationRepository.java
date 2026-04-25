package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {
    @Query("SELECT a FROM ApplicationEntity a " +
            "WHERE a.personalData.passportSeries = :series " +
            "AND a.personalData.passportNumber = :number " +
            "AND a.personalData.active = true")
    List<ApplicationEntity> findByPassportSeriesAndNumber(@Param("series") String series, @Param("number") String number);
}
