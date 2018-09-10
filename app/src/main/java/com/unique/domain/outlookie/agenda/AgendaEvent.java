package com.unique.domain.outlookie.agenda;

import com.unique.domain.outlookie.core.User;
import com.unique.domain.outlookie.storage.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AgendaEvent implements AgendaItem {
    final Long id;
    LocalDateTime startTimeDate;
    LocalDateTime endTimeDate;
    String title;
    String description;
    String location;
    AgendaEventIcon icon;
    User creator;
    List<User> participants;

    public AgendaEvent(Long id, LocalDateTime startTimeDate, LocalDateTime endTimeDate, String title, String description, String location, AgendaEventIcon icon, User creator, List<User> participants) {
        this.id = id;
        this.startTimeDate = startTimeDate;
        this.endTimeDate = endTimeDate;
        this.title = title;
        this.description = description;
        this.location = location;
        this.icon = icon;
        this.creator = creator;
        this.participants = participants;
    }

    public AgendaEvent(Event event) {
        this.id = event.getEventId();
        this.startTimeDate = event.getStartDateTime();
        this.endTimeDate = event.getEndDateTime();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.icon = new AgendaEventIcon(event.getIconColor());
        // TODO: this.creator
        // TODO: this.participants
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTimeDate() {
        return startTimeDate;
    }

    public LocalDateTime getEndTimeDate() {
        return endTimeDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public AgendaEventIcon getIcon() {
        return icon;
    }

    public User getCreator() {
        return creator;
    }

    public List<User> getParticipants() {
        return participants;
    }

    @Override
    public LocalDate getDate() {
        return getStartTimeDate().toLocalDate();
    }
}
