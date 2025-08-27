package com.web.jaru.certifications.service;

import com.web.jaru.certifications.repository.custom.CertificationCustomRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CertificationService {

    private final CertificationCustomRepositoryImpl certificationCustomRepositoryImpl;
}
