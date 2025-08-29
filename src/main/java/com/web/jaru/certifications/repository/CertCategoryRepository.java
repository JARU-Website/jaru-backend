package com.web.jaru.certifications.repository;

import com.web.jaru.certifications.domain.CertCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertCategoryRepository extends JpaRepository<CertCategory, Long> {

    public Optional<CertCategory> findByName(String name);
}
