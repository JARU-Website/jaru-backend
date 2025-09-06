package com.web.jaru.certifications.repository.custom;

import com.web.jaru.certifications.dto.CertScheduleDTO;
import com.web.jaru.users.domain.User;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface CertScheduleCustomRepository {
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMyCertSchedule(User user, YearMonth yearMonth, boolean isAlarmed);
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMyCertScheduleByDay(User user, LocalDate start, LocalDate end, boolean isAlarmed);
}
