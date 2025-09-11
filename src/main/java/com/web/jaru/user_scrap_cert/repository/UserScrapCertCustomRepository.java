package com.web.jaru.user_scrap_cert.repository;

import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.certifications.dto.CertDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserScrapCertCustomRepository {
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> findScrapCertList(Long userId, int page, int size);
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> findScrapCertListWithCategory(List<Long> certCategoryIds, Long userId, int page, int size);
}
