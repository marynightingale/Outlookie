package com.unique.domain.outlookie.agenda;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class AgendaItemStoreTest {

    @Test
    public void initStoreWithOneEvent_DayTitlePresent() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 11, 11);
        AgendaEvent event = createEvent(dateTime);

        AgendaItemStore store = new AgendaItemStore(Arrays.asList(event));

        List<AgendaItem> items = store.getAgendaItems();

        assertTrue(items.get(0) instanceof AgendaTitle);
        assertDatesMatch(dateTime, items.get(0).getDateTime());
        assertEquals(event, items.get(1));
    }

    @Test
    public void initStoreWithNoEvents_DayTitleAndPlaceholderPresent() {
        LocalDateTime dateTime = LocalDateTime.now();

        AgendaItemStore store = new AgendaItemStore(Arrays.asList());

        List<AgendaItem> items = store.getAgendaItems();

        assertTrue(items.get(0) instanceof AgendaTitle);
        assertDatesMatch(dateTime, items.get(0).getDateTime());
        assertTrue(items.get(1) instanceof AgendaNoEventsPlaceholder);
    }

    @Test
    public void initStoreWithTwoEventsOnSameDay_OneDayTitlePresent() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 11, 11);
        AgendaEvent event1 = createEvent(dateTime);
        AgendaEvent event2 = createEvent(dateTime.plusHours(2));

        AgendaItemStore store = new AgendaItemStore(Arrays.asList(event1, event2));
        List<AgendaItem> items = store.getAgendaItems();

        assertTrue(items.get(0) instanceof AgendaTitle);
        assertDatesMatch(dateTime, items.get(0).getDateTime());

        assertEquals(event1, items.get(1));
        assertEquals(event2, items.get(2));
    }

    @Test
    public void initStoreWithTwoEventsSubsequentDays_TwoDayTitlesPresent() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 11, 11);
        AgendaEvent event1 = createEvent(dateTime);
        AgendaEvent event2 = createEvent(dateTime.plusDays(1));

        AgendaItemStore store = new AgendaItemStore(Arrays.asList(event1, event2));
        List<AgendaItem> items = store.getAgendaItems();

        assertTrue(items.get(0) instanceof AgendaTitle);
        assertDatesMatch(dateTime, items.get(0).getDateTime());

        assertEquals(event1, items.get(1));

        assertTrue(items.get(2) instanceof AgendaTitle);
        assertDatesMatch(dateTime.plusDays(1), items.get(2).getDateTime());

        assertEquals(event2, items.get(3));
    }

    @Test
    public void initStoreWithTwoEventsNotSubsequentDays_EventPlaceholderPresent() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 10, 11, 11);
        AgendaEvent event1 = createEvent(dateTime);
        AgendaEvent event2 = createEvent(dateTime.plusDays(2));

        AgendaItemStore store = new AgendaItemStore(Arrays.asList(event1, event2));
        List<AgendaItem> items = store.getAgendaItems();

        assertTrue(items.get(0) instanceof AgendaTitle);
        assertDatesMatch(dateTime, items.get(0).getDateTime());

        assertEquals(event1, items.get(1));

        assertTrue(items.get(2) instanceof AgendaTitle);
        assertDatesMatch(dateTime.plusDays(1), items.get(2).getDateTime());

        assertTrue(items.get(3) instanceof AgendaNoEventsPlaceholder);

        assertTrue(items.get(4) instanceof AgendaTitle);
        assertDatesMatch(dateTime.plusDays(2), items.get(4).getDateTime());

        assertEquals(event2, items.get(5));
    }

    private AgendaEvent createEvent(LocalDateTime dateTime) {
        return new AgendaEvent(UUID.randomUUID(), dateTime, dateTime.plusHours(1), "title", "decription", null, null, null, null);
    }

    private void assertDatesMatch(LocalDateTime expected, LocalDateTime actual) {
        assertEquals(expected.getYear(), actual.getYear());
        assertEquals(expected.getMonth(), actual.getMonth());
        assertEquals(expected.getDayOfMonth(), actual.getDayOfMonth());
    }

}