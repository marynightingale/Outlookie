package com.unique.domain.outlookie.agenda;

import android.content.Context;

import com.unique.domain.outlookie.storage.Event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.unique.domain.outlookie.TestUtils.createEvent;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AgendaAdapterTest {

    @Mock
    Context mockContext;

    @Mock
    AgendaItemStore agendaItemStore;

    @Test
    public void getItemFromAgendaAdapter_ItemFromItemStoreRetrieved() {
        AgendaAdapter agendaAdapter = new AgendaAdapter(agendaItemStore);
        for (int i = 0; i < 10; i++) {
            agendaAdapter.get(i);
            verify(agendaItemStore).get(i);
        }
    }

    @Test
    public void getPositionOfAgendaTitleFromAgendaAdapter_AgendaTitleFromItemStoreRetrieved() {
        AgendaAdapter agendaAdapter = new AgendaAdapter(agendaItemStore);
        LocalDate startDate = LocalDate.now();
        for (LocalDate date = startDate; date.isBefore(startDate.plusMonths(1)); date = date.plusDays(5)) {
            agendaAdapter.getPositionOfAgendaTitle(date);
            verify(agendaItemStore).getPositionOfAgendaTitle(date);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void onCreateViewHolder() {
        AgendaAdapter agendaAdapter = new AgendaAdapter(null);
    }
    
}