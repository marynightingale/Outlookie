package com.unique.domain.outlookie.agenda;

import android.support.annotation.NonNull;

import com.unique.domain.outlookie.core.DateUtils;
import com.unique.domain.outlookie.storage.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AgendaItemStore {

    private List<AgendaItem> agendaItems;

    public AgendaItemStore(List<Event> events) {
        updateEvents(events);
    }

    public void updateEvents(List<Event> events) {
        agendaItems = new ArrayList<>();

        if (events == null) {
            events = Collections.EMPTY_LIST;
        }

        // this should really be a lazy list that can scroll forever, but alas
        // My cheap solution would be to start from the week of the oldest event instead
        LocalDate firstDate;
        if (events.isEmpty()) {
            firstDate = LocalDate.now();
        }
        else {
            events.sort(Comparator.comparing(Event::getStartDateTime));
            firstDate = events.get(0).getStartDateTime().toLocalDate();
        }

        firstDate = DateUtils.getFirstDayOfTheWeek(firstDate);

        addAgendaTitle(agendaItems, firstDate);

        if (events.isEmpty()) {
            addEventPlaceholder(agendaItems, firstDate);
        } else {
            addEventsAsAgendaItems(events, 0);
        }
    }

    public AgendaItem get(int position) {
        return agendaItems.get(position);
    }

    public int size() {
        return agendaItems.size();
    }

    public int getPositionOfAgendaTitle(LocalDate date) {
        // if date is before the first existing one, return the top item
        LocalDate firstDate = agendaItems.get(0).getDate();
        if (firstDate.isAfter(date)) {
            return 0;
        }

        for (int i = 0; i < size(); i++) {
            AgendaItem item = agendaItems.get(i);
            if (item instanceof AgendaTitle && item.getDate().isEqual(date)) {
                return i;
            }
        }
        // no matching titles found - return the very last one
        return size()-1;
    }

    private void addEventsAsAgendaItems(List<Event> events, int position) {
        if (position == events.size()) {
            return;
        }
        AgendaItem agendaItem = agendaItems.get(agendaItems.size() - 1);

        LocalDateTime currentDateTime = events.get(position).getStartDateTime();
        LocalDate previousDate = agendaItem.getDate();

        if (previousDate.isEqual(currentDateTime.toLocalDate())) {
            AgendaEvent agendaEvent = new AgendaEvent(events.get(position));
            agendaItems.add(agendaEvent);
            addEventsAsAgendaItems(events, position+1);
        }
        else {
            if (agendaItem instanceof AgendaTitle) {
                addEventPlaceholder(agendaItems, previousDate);
            }
            addAgendaTitle(agendaItems, previousDate.plusDays(1));
            addEventsAsAgendaItems(events, position);
        }
    }

    private static boolean isOnSameDay(LocalDateTime dateTime, LocalDateTime otherDateTime) {
        return  (dateTime.getYear() == otherDateTime.getYear()) &&
                (dateTime.getDayOfYear() == otherDateTime.getDayOfYear());
    }

    private static void addAgendaTitle(List<AgendaItem> agendaItems, LocalDate date) {
        agendaItems.add(new AgendaTitle(date));
    }

    private static void addEventPlaceholder(List<AgendaItem> agendaItems, LocalDate date) {
        agendaItems.add(new AgendaNoEventsPlaceholder(date));
    }
}
