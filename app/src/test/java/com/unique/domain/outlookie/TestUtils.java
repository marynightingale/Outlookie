package com.unique.domain.outlookie;

import com.unique.domain.outlookie.storage.Event;

import java.time.LocalDateTime;

public class TestUtils {
    public static Event createEvent(LocalDateTime dateTime) {
        Event event = new Event();
        event.setStartDateTime(dateTime);
        return event;
//        return new Event(UUID.randomUUID(), dateTime, dateTime.plusHours(1), "title", "decription", null, null, null, null);
    }
}
