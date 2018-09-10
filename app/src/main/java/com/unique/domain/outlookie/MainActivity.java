package com.unique.domain.outlookie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.unique.domain.outlookie.agenda.AgendaAdapter;
import com.unique.domain.outlookie.agenda.AgendaItem;
import com.unique.domain.outlookie.agenda.AgendaItemStore;
import com.unique.domain.outlookie.calendar.CalendarAdapter;
import com.unique.domain.outlookie.calendar.CalendarHeader;
import com.unique.domain.outlookie.storage.Event;
import com.unique.domain.outlookie.storage.EventDatabase;
import com.unique.domain.outlookie.storage.EventViewModel;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EventViewModel eventViewModel;

    private RecyclerView calendarRecyclerView;
    private CalendarAdapter calendarAdapter;

    private RecyclerView agendaRecyclerView;
    private AgendaAdapter agendaAdapter;
    private LinearLayoutManager agendaLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: pop up dialog
                Event event = new Event(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), "Ad Hoc Meeting", "desc", "Focus Room", Color.parseColor("#AD7A99"));
                eventViewModel.insert(event);
            }
        });

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventViewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable final List<Event> events) {
                // Update the cached copy of the words in the adapter.
                agendaAdapter.updateEvents(events);
            }
        });

        setUpCalendar();
        setUpAgenda();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpCalendar() {

        LinearLayout calendarView = findViewById(R.id.CalendarHeader);
        calendarView.setOrientation(LinearLayout.HORIZONTAL);

        CalendarHeader calendarHeader = new CalendarHeader();
        calendarHeader.populateView(calendarView);

        calendarRecyclerView = findViewById(R.id.CalendarView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        calendarRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        calendarRecyclerView.setLayoutManager(layoutManager);

        GradientDrawable selectedCellBackground = (GradientDrawable) getResources().getDrawable(R.drawable.calendar_circle_cell, getTheme());

        // specify an adapter
        calendarAdapter = new CalendarAdapter(
                LocalDate.now(), new CalendarAdapter.OnItemClickListener() {
                    @Override public void onItemClick(LocalDate date) {
                        int position = agendaAdapter.getPositionOfAgendaTitle(date);
                        agendaLayoutManager.scrollToPositionWithOffset(position, 0);
                        }
                });
        calendarRecyclerView.setAdapter(calendarAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                calendarRecyclerView.getContext(),
                layoutManager.getOrientation()
        );
        calendarRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void setUpAgenda() {
        agendaRecyclerView = findViewById(R.id.AgendaView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        agendaRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        agendaLayoutManager = new LinearLayoutManager(this);
        agendaRecyclerView.setLayoutManager(agendaLayoutManager);

        // specify an adapter (see also next example)
        AgendaItemStore itemStore = new AgendaItemStore(this.eventViewModel.getEvents().getValue());
        agendaAdapter = new AgendaAdapter(itemStore);
        agendaRecyclerView.setAdapter(agendaAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                agendaRecyclerView.getContext(),
                agendaLayoutManager.getOrientation()
        );
        agendaRecyclerView.addItemDecoration(dividerItemDecoration);

        agendaRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                int firstVisibleItemPosition = agendaLayoutManager.findFirstVisibleItemPosition();
                AgendaItem item = agendaAdapter.get(firstVisibleItemPosition);
                setSelectedDateInCalendar(item.getDate());
            }
        });

        // Move agenda and calendar to today's date after activity is loaded
        // Select today after layout finished drawing

        agendaRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //At this point the layout is complete
                LocalDate date = LocalDate.now();
                int position = agendaAdapter.getPositionOfAgendaTitle(date);

                AgendaItem item = agendaAdapter.get(position);
                if (!item.getDate().equals(date)) { // ugly hack, because data doesn't completely load still
                    return;
                }

                agendaLayoutManager.scrollToPositionWithOffset(position, 0);

                // needs to be done once at the start, so now we unsubscribe
                agendaRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    private void setSelectedDateInCalendar(LocalDate date) {
        CalendarAdapter.ViewHolder viewHolder = (CalendarAdapter.ViewHolder) calendarRecyclerView.findViewHolderForAdapterPosition(calendarAdapter.getPositionByDate(date));
        calendarAdapter.setSelectedDate(viewHolder, date);
    }
}
