package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.PersonalDataEntity;
import com.mr_syrus.credit.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PersonalDataRepository extends JpaRepository<PersonalDataEntity, Integer> {

    boolean existsByPhone(String phone);
    Optional<PersonalDataEntity> findByUser(UserEntity user);



    @Query("SELECT pd FROM PersonalDataEntity pd " +
            "JOIN pd.user u " +
            "WHERE u.mail = :mail " +
            "AND pd.passportSeries = :series " +
            "AND pd.passportNumber = :number " +
            "AND pd.active = true")
    Optional<PersonalDataEntity> findActiveByMailAndPassportData(
            @Param("mail") String mail,
            @Param("series") String series,
            @Param("number") String number);

    @Query("SELECT COUNT(pd) > 0 FROM PersonalDataEntity pd " +
            "WHERE pd.passportSeries = :series " +
            "AND pd.passportNumber = :number " +
            "AND pd.active = true")
    boolean existsActiveByPassport(
            @Param("series") String series,
            @Param("number") String number);
}