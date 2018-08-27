package com.unique.domain.outlookie.calendar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.transition.Visibility;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unique.domain.outlookie.R;
import com.unique.domain.outlookie.core.Circle;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DAYS_IN_A_WEEK = 7;
    private static final int ROWS_IN_CALENDAR = 2;

    private int weekCount;
    private LocalDateTime selectedDate;
    private ViewHolder selectedDateViewHolder;

    private final LocalDateTime initialDate;
    private final OnItemClickListener globalListener;

    public CalendarAdapter(LocalDateTime initialDate, OnItemClickListener globalListener) {
        this.weekCount = ROWS_IN_CALENDAR;

        // from documentation: the day-of-week, from 1 (Monday) to 7 (Sunday)
        int dayOfWeek = initialDate.getDayOfWeek().getValue();
        LocalDateTime initialFirstDisplayDate = initialDate.minusDays(dayOfWeek % DAYS_IN_A_WEEK);
        this.initialDate = initialFirstDisplayDate;

        this.globalListener = globalListener;
    }

    public interface OnItemClickListener {
        void onItemClick(LocalDateTime date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final int unselectedDateTextColor = Color.parseColor("#949399"); // TODO: get from colors resource
        private static final int unselectedDateBackgroundColor = Color.WHITE;
        private static final int selectedDateTextColor = Color.WHITE;
        private static final int selectedDateBackgroundColor = Color.parseColor("#0078D8"); // TODO: get from colors resource

        LinearLayout row;
        private final ShapeDrawable unselectedCellBackground;
        private final ShapeDrawable selectedCellBackground;

        public ViewHolder(LinearLayout calendarRow) {
            super(calendarRow);
            row = itemView.findViewById(R.id.CalendarHeader);
            this.selectedCellBackground = Circle.draw(50, selectedDateBackgroundColor);
            this.unselectedCellBackground = Circle.draw(50, unselectedDateBackgroundColor);
        }

        public void set(LocalDateTime startDate, final OnItemClickListener listener) {
            for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
                TextView cell = (TextView)row.getChildAt(i);
                LocalDateTime cellDate = startDate.plusDays(i);

                cell.setText(String.valueOf(cellDate.getDayOfMonth()));

                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle(cellDate.getDayOfWeek());
                        listener.onItemClick(cellDate);
                    }
                });
            }
        }

        public void toggle(DayOfWeek dayOfWeek) {
            int position = dayOfWeek.getValue() % DAYS_IN_A_WEEK;
            TextView cell = (TextView)row.getChildAt(position);

            // not the best selection indicator :/
            if (cell.getCurrentTextColor() == unselectedDateTextColor) {
                cell.setTextColor(selectedDateTextColor);
                cell.setBackground(selectedCellBackground);
            }
            else {
                cell.setTextColor(unselectedDateTextColor);
                cell.setBackground(unselectedCellBackground);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LinearLayout calendarRowView = (LinearLayout) layoutInflater.inflate(R.layout.calendar_row, parent, false);
        setNewCalendarRow(calendarRowView, layoutInflater);

        ViewHolder viewHolder = new ViewHolder(calendarRowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // position = row number = number of weeks from the start
        LocalDateTime firstDisplayDate = initialDate.plusWeeks(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.set(firstDisplayDate, new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LocalDateTime date) {
                if (selectedDateViewHolder != null) {
                    selectedDateViewHolder.toggle(selectedDate.getDayOfWeek());
                }
                setSelectedDate(viewHolder, date);
                globalListener.onItemClick(date);
            }
        });
    }

    public void setSelectedDate(ViewHolder viewHolder, LocalDateTime date) {
        selectedDateViewHolder = viewHolder;
        selectedDate = date;
    }

    @Override
    public int getItemCount() {
        return weekCount;
    }

    private void setNewCalendarRow(LinearLayout calendarRowView, LayoutInflater layoutInflater) {
        calendarRowView.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
            View calendarCell = layoutInflater.inflate(R.layout.calendar_cell, calendarRowView, false);
            calendarRowView.addView(calendarCell);
        }
    }
}
