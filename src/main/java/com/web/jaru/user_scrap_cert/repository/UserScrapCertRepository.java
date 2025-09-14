package com.web.jaru.user_scrap_cert.repository;

import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.user_scrap_cert.domain.UserScrapCert;
import com.web.jaru.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserScrapCertRepository extends JpaRepository<UserScrapCert, Long>, UserScrapCertCustomRepository {

    boolean existsByUserAndCertification(User user, Certification certification);
    void deleteByUserAndCertification(User user, Certification certification);

    List<UserScrapCert> findByUser(User user);
}
