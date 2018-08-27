package com.unique.domain.outlookie;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.unique.domain.outlookie.agenda.AgendaAdapter;
import com.unique.domain.outlookie.agenda.AgendaEvent;
import com.unique.domain.outlookie.agenda.AgendaEventIcon;
import com.unique.domain.outlookie.calendar.CalendarAdapter;
import com.unique.domain.outlookie.calendar.CalendarHeader;
import com.unique.domain.outlookie.core.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView calendarRecyclerView;
    private RecyclerView agendaRecyclerView;
    private LinearLayoutManager agendaLayoutManager;
    private AgendaAdapter agendaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        // specify an adapter (see also next example)
        CalendarAdapter adapter = new CalendarAdapter(
                LocalDateTime.now(),
                new CalendarAdapter.OnItemClickListener() {
                @Override public void onItemClick(LocalDateTime date) {
                    int position = agendaAdapter.getAgendaItemStore().getPositionOfAgendaTitle(date);
                    agendaLayoutManager.scrollToPositionWithOffset(position, 0);
                    }
                });
        calendarRecyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                calendarRecyclerView.getContext(),
                layoutManager.getOrientation()
        );
        calendarRecyclerView.addItemDecoration(dividerItemDecoration);

        // Select today after layout finished drawing
        calendarRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //At this point the layout is complete
                LocalDateTime date = LocalDateTime.now();
                CalendarAdapter.ViewHolder viewHolder = (CalendarAdapter.ViewHolder) calendarRecyclerView.findViewHolderForAdapterPosition(0);

                // TODO: remove logic duplication
                viewHolder.toggle(date.getDayOfWeek());
                adapter.setSelectedDate(viewHolder, date);

                // needs to be done once at the start, so now we unsubscribe
                calendarRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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
        agendaAdapter = new AgendaAdapter(getEvents());
        agendaRecyclerView.setAdapter(agendaAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                agendaRecyclerView.getContext(),
                agendaLayoutManager.getOrientation()
        );
        agendaRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private List<AgendaEvent> getEvents() {
        List<AgendaEvent> events = new ArrayList<>();

        events.add(new AgendaEvent(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), "meeting1", "desc", "Golden Gate", new AgendaEventIcon(Color.parseColor("#AD7A99")), new User(), null));
        events.add(new AgendaEvent(UUID.randomUUID(), LocalDateTime.now().minusHours(1), LocalDateTime.now(), "meeting2", "desc", null, new AgendaEventIcon(Color.parseColor("#4E937A")), new User(), null));
        events.add(new AgendaEvent(UUID.randomUUID(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "meeting3", "desc", "Blue Bottle", new AgendaEventIcon(Color.parseColor("#414770")), new User(), null));
        events.add(new AgendaEvent(UUID.randomUUID(), LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(1).plusMinutes(30), "meeting4", "desc", null, new AgendaEventIcon(Color.parseColor("#414770")), new User(), null));
        events.add(new AgendaEvent(UUID.randomUUID(), LocalDateTime.now().plusDays(6), LocalDateTime.now().plusDays(4).plusHours(1).plusMinutes(30), "meeting4", "desc", null, new AgendaEventIcon(Color.parseColor("#4E937A")), new User(), null));
        events.add(new AgendaEvent(UUID.randomUUID(), LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(4).plusHours(1).plusMinutes(30), "meeting4", "desc", null, new AgendaEventIcon(Color.parseColor("#559CAD")), new User(), null));
        events.add(new AgendaEvent(UUID.randomUUID(), LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(4).plusHours(1).plusMinutes(30), "meeting4", "desc", null, new AgendaEventIcon(Color.parseColor("#559CAD")), new User(), null));

        return events;
    }
}
