package com.unique.domain.outlookie.core;

import java.time.LocalDate;

public class DateUtils {

    public static final int DAYS_IN_A_WEEK = 7;

    public static LocalDate getFirstDayOfTheWeek(LocalDate date) {
        // from documentation: the day-of-week, from 1 (Monday) to 7 (Sunday)
        int dayOfWeek = date.getDayOfWeek().getValue();
        return date.minusDays(dayOfWeek % DateUtils.DAYS_IN_A_WEEK);
    }
}
