package com.unique.domain.outlookie.agenda;

import com.unique.domain.outlookie.storage.Event;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.unique.domain.outlookie.TestUtils.createEvent;
import static org.junit.Assert.*;

public class AgendaItemStoreTest {

    @Test
    public void initStoreWithOneEvent_DayTitlePresent() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 11, 11);
        Event event = createEvent(dateTime);

        AgendaItemStore store = new AgendaItemStore(Arrays.asList(event));

        assertTitle(store.get(0), dateTime);
        assertEvent(store.get(1), event);
    }

    @Test
    public void initStoreWithNoEvents_DayTitleAndPlaceholderPresent() {
        LocalDateTime dateTime = LocalDateTime.now();

        AgendaItemStore store = new AgendaItemStore(Arrays.asList());

        assertTitle(store.get(0), dateTime);
        assertPlaceholder(store.get(1));
    }

    @Test
    public void initStoreWithTwoEventsOnSameDay_OneDayTitlePresent() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 11, 11);
        Event event1 = createEvent(dateTime);
        Event event2 = createEvent(dateTime.plusHours(2));

        AgendaItemStore store = new AgendaItemStore(Arrays.asList(event1, event2));

        assertTitle(store.get(0), dateTime);
        assertEvent(store.get(1), event1);
        assertEvent(store.get(2), event2);
    }

    @Test
    public void initStoreWithTwoEventsSubsequentDays_TwoDayTitlesPresent() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 11, 11);
        Event event1 = createEvent(dateTime);
        Event event2 = createEvent(dateTime.plusDays(1));

        AgendaItemStore store = new AgendaItemStore(Arrays.asList(event1, event2));

        assertTitle(store.get(0), dateTime);
        assertEvent(store.get(1), event1);
        assertTitle(store.get(2), dateTime.plusDays(1));
        assertEvent(store.get(3), event2);
    }

    @Test
    public void initStoreWithTwoEventsNotSubsequentDays_EventPlaceholderPresent() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 11, 11);
        Event event1 = createEvent(dateTime);
        Event event2 = createEvent(dateTime.plusDays(2));

        AgendaItemStore store = new AgendaItemStore(Arrays.asList(event1, event2));

        assertTitle(store.get(0), dateTime);
        assertEvent(store.get(1), event1);
        assertTitle(store.get(2), dateTime.plusDays(1));
        assertPlaceholder(store.get(3));
        assertTitle(store.get(4), dateTime.plusDays(2));
        assertEvent(store.get(5), event2);
    }

    private void assertDatesMatch(LocalDateTime expected, LocalDateTime actual) {
        assertEquals(expected.getYear(), actual.getYear());
        assertEquals(expected.getMonth(), actual.getMonth());
        assertEquals(expected.getDayOfMonth(), actual.getDayOfMonth());
    }

    private void assertTitle(AgendaItem item, LocalDateTime dateTime) {
        assertTrue(item instanceof AgendaTitle);
        assertEquals(dateTime.toLocalDate(), item.getDate());
    }

    private void assertEvent(AgendaItem item, Event event) {
        assertTrue(item instanceof AgendaEvent);

        AgendaEvent agendaEvent = (AgendaEvent) item;
        assertEquals(agendaEvent.startTimeDate, event.getStartDateTime());
        assertEquals(agendaEvent.title, event.getTitle());
    }

    private void assertPlaceholder(AgendaItem item) {
        assertTrue(item instanceof AgendaNoEventsPlaceholder);
    }

}