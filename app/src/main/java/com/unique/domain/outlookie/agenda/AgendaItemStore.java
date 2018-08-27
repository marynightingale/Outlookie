package com.unique.domain.outlookie.agenda;

import android.support.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AgendaItemStore {

    private List<AgendaItem> agendaItems;

    public AgendaItemStore(@NonNull List<AgendaEvent> events) {
        agendaItems = new ArrayList<>();
        events.sort(Comparator.comparing(AgendaEvent::getStartTimeDate));

        // this should really be a lazy list that can scroll forever, but alas
        // My cheap solution would be to start from the oldest event instead
        LocalDateTime firstDate;
        if (events.isEmpty()) {
            firstDate = LocalDateTime.now();
        }
        else {
            firstDate = events.get(0).getStartTimeDate();
        }

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

    // a little recursion to impress the interviewer ;)
    private void addEventsAsAgendaItems(List<AgendaEvent> events, int position) {
        if (position == events.size()) {
            return;
        }
        AgendaItem agendaItem = agendaItems.get(agendaItems.size() - 1);

        LocalDateTime currentDateTime = events.get(position).getStartTimeDate();
        LocalDateTime previousDateTime = agendaItem.getDateTime();

        if (isOnSameDay(previousDateTime, currentDateTime)) {
            agendaItems.add(events.get(position));
            addEventsAsAgendaItems(events, position+1);
        }
        else {
            if (agendaItem instanceof AgendaTitle) {
                addEventPlaceholder(agendaItems, previousDateTime);
            }
            addAgendaTitle(agendaItems, previousDateTime.plusDays(1));
            addEventsAsAgendaItems(events, position);
        }
    }

    public int getPositionOfAgendaTitle(LocalDateTime date) {
        // if date is before the first existing one, return the top item
        LocalDateTime firstDate = agendaItems.get(0).getDateTime();
        if (firstDate.isAfter(date) &&
                !isOnSameDay(firstDate, date)) {
            return 0;
        }

        for (int i = 0; i < size(); i++) {
            AgendaItem item = agendaItems.get(i);
            if (item instanceof AgendaTitle && isOnSameDay(item.getDateTime(), date)) {
                return i;
            }
        }
        // no matching titles found - return the very last one
        return size();
    }

    private static boolean isOnSameDay(LocalDateTime dateTime, LocalDateTime otherDateTime) {
        return  (dateTime.getYear() == otherDateTime.getYear()) &&
                (dateTime.getDayOfYear() == otherDateTime.getDayOfYear());
    }

    private static void addAgendaTitle(List<AgendaItem> agendaItems, LocalDateTime dateTime) {
        agendaItems.add(new AgendaTitle(dateTime));
    }

    private static void addEventPlaceholder(List<AgendaItem> agendaItems, LocalDateTime dateTime) {
        agendaItems.add(new AgendaNoEventsPlaceholder(dateTime));
    }
}
