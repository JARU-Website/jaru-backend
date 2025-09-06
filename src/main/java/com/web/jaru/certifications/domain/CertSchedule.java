package com.web.jaru.certifications.domain;

import com.web.jaru.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "certification_schedule")
public class CertSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @Column(name = "apply_from", nullable = false)
    private LocalDate applyFrom;

    @Column(name = "apply_to", nullable = false)
    private LocalDate applyTo;

    @Column(name = "test_date_from", nullable = false)
    private LocalDate testDateFrom;

    @Column(name = "test_date_to")
    private LocalDate testDateTo;

    @Column(name = "result_date")
    private LocalDate resultDate;
}
