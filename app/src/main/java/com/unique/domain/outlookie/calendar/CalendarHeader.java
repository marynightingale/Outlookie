package com.unique.domain.outlookie.calendar;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unique.domain.outlookie.R;

public class CalendarHeader {

    private CalendarHeaderItem[] days = {
            new CalendarHeaderItem("S", false),
            new CalendarHeaderItem("M", true),
            new CalendarHeaderItem("T", true),
            new CalendarHeaderItem("W", true),
            new CalendarHeaderItem("T", true),
            new CalendarHeaderItem("F", true),
            new CalendarHeaderItem("S", false),
    };

    private class CalendarHeaderItem {
        String day;
        boolean isWeekday;

        public CalendarHeaderItem(String day, boolean isWeekday) {
            this.day = day;
            this.isWeekday = isWeekday;
        }
    }

    public void populateView(@NonNull ViewGroup parent) {
        for (CalendarHeaderItem day : days) {
            View calendarCell = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_header_cell, parent, false);
            parent.addView(calendarCell);
            TextView textView = calendarCell.findViewById(R.id.CalendarHeaderCell);
            textView.setText(day.day);
            if (day.isWeekday) {
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            }
        }
    }
}
