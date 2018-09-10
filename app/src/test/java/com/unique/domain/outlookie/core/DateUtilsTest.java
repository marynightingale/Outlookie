package com.unique.domain.outlookie.core;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class DateUtilsTest {

    LocalDate sunday = LocalDate.of(2018, 9, 9);

    @Test
    public void getFirstDayOfTheWeek_Sunday_ReturnsSunday() {
        LocalDate date = sunday;
        LocalDate firstDayOfTheWeek = DateUtils.getFirstDayOfTheWeek(date);

        assertEquals(sunday, firstDayOfTheWeek);
    }

    @Test
    public void getFirstDayOfTheWeek_Weekday_ReturnsSunday() {
        for (int i = 1; i < 6; i++) {
            LocalDate date = sunday.plusDays(i);
            LocalDate firstDayOfTheWeek = DateUtils.getFirstDayOfTheWeek(date);
            assertEquals(sunday, firstDayOfTheWeek);
        }
    }

    @Test
    public void getFirstDayOfTheWeek_NextSunday_ReturnsNextSunday() {
        LocalDate date = sunday.plusWeeks(1);
        LocalDate firstDayOfTheWeek = DateUtils.getFirstDayOfTheWeek(date);

        assertEquals(date, firstDayOfTheWeek);
    }

}