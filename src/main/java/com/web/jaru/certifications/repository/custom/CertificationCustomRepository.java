package com.web.jaru.certifications.repository.custom;

import com.web.jaru.certifications.domain.CertCategory;
import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.certifications.dto.CertDTO;
import com.web.jaru.users.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CertificationCustomRepository {

    public List<CertDTO.PopularCertResponse> viewPopularCertByCategory(Long categoryId);
    public List<Certification> findScrappedCertifications();
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> viewTopCertByCategory(
            User user, Long[] categoryList, int page, int size);
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> viewRecentCertByCategory(
            User user, Long[] categoryList, int page, int size);
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> searchCerts(
            User user, String keyword, Long[] categoryList, int page, int size);

}
