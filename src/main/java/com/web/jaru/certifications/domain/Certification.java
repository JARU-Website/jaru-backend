package com.web.jaru.certifications.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id;

    private String name; // 자격증명
    private String issuer; // 시행기관
    private String certType; // 자격 구분
    private String officialUrl; // 공식 사이트
    @Lob
    private String testOverview; // 시험 개요
    private String fee; // 응시료
    private Short difficulty; // 난이도
    private String testContents; // 시험 내용
    private String passingScore; // 합격기준

    @ManyToOne(fetch = FetchType.LAZY, optional = true) // category_id NULL 허용
    @JoinColumn(name = "cert_category_id") // FK 제약이 없어도 무방, 있으면 그대로 사용
    private CertCategory certCategory;
}
