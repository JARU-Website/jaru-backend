package com.web.jaru.certifications.repository;

import com.web.jaru.certifications.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    Optional<Certification> findById(Long id);
    Optional<Certification> findByName(String name);

}
