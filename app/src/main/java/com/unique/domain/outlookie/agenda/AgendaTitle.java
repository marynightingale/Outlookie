package com.unique.domain.outlookie.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AgendaTitle implements AgendaItem {
    private LocalDate date;

    public AgendaTitle(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}

