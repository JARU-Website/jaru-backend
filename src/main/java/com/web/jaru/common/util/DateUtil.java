package com.web.jaru.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtil {

    public static String getKoreanDayOfWeek(LocalDate date) {
        if (date == null) return null;

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> "월요일";
            case TUESDAY -> "화요일";
            case WEDNESDAY -> "수요일";
            case THURSDAY -> "목요일";
            case FRIDAY -> "금요일";
            case SATURDAY -> "토요일";
            case SUNDAY -> "일요일";
        };
    }
}
