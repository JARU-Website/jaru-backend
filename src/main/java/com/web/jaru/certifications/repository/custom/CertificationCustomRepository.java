package com.web.jaru.certifications.repository.custom;

import com.web.jaru.certifications.domain.CertCategory;
import com.web.jaru.certifications.domain.Certification;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CertificationCustomRepository {

    public List<Certification> findAll(Pageable pageable);
    public List<Certification> findByCertCategory(CertCategory certCategory, Pageable pageable);
    public List<Certification> findScrappedCertifications();
}
