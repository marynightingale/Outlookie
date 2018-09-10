package com.unique.domain.outlookie.storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.unique.domain.outlookie.agenda.AgendaEventIcon;
import com.unique.domain.outlookie.core.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Event {

    @PrimaryKey(autoGenerate = true)
    private Long eventId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String title;
    private String description;
    private String location;
    private int iconColor;

    public Event() {
    }

    public Event(LocalDateTime startDateTime, LocalDateTime endDateTime, String title, String description, String location, int iconColor) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.title = title;
        this.description = description;
        this.location = location;
        this.iconColor = iconColor;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setEventTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
    }
}

