package com.unique.domain.outlookie.agenda;

import java.time.LocalDateTime;

public class AgendaTitle implements AgendaItem {
    private LocalDateTime dateTime;

    public AgendaTitle(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

