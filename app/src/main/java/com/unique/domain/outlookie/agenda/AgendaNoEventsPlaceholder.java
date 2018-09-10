package com.unique.domain.outlookie.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AgendaNoEventsPlaceholder implements AgendaItem {
    private LocalDate date;

    public AgendaNoEventsPlaceholder(LocalDate date) {
        this.date = date;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }
}
