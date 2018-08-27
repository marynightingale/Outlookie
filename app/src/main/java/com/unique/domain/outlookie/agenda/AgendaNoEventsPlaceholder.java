package com.unique.domain.outlookie.agenda;

import java.time.LocalDateTime;

public class AgendaNoEventsPlaceholder implements AgendaItem {
    private LocalDateTime dateTime;

    public AgendaNoEventsPlaceholder(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
