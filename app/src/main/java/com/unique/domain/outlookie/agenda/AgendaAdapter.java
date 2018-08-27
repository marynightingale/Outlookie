package com.unique.domain.outlookie.agenda;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unique.domain.outlookie.R;
import com.unique.domain.outlookie.core.Circle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int VIEW_TYPE_TITLE = 0;
    final int VIEW_TYPE_EVENT = 1;
    final int VIEW_TYPE_NO_EVENT = 2;

    private AgendaItemStore agendaItemStore;

    public class AgendaTitleViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public AgendaTitleViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        public void setTitle(LocalDateTime dateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d");
            title.setText(dateTime.format(formatter));
        }
    }

    public class AgendaEventViewHolder extends RecyclerView.ViewHolder {

        private TextView time;
        private TextView duration;
        private ImageView icon;
        private TextView title;
        private TextView location;

        public AgendaEventViewHolder(final View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            duration = itemView.findViewById(R.id.duration);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            location = itemView.findViewById(R.id.location);
        }

        public void set(AgendaEvent event) {
            String startTime = event.getStartTimeDate().format(DateTimeFormatter.ofPattern("h:ma"));
            time.setText(startTime);

            Duration eventDuration = Duration.between(event.getStartTimeDate(), event.getEndTimeDate());
            duration.setText(durationToString(eventDuration));

            icon.setImageDrawable(Circle.draw(50, event.getIcon().color));

            title.setText(event.getTitle());

            if (event.getLocation() != null) {
                location.setText("\u25BD " + event.getLocation());
            }

        }

        private String durationToString(Duration duration) {
            String durationString = "";

            long hours = duration.toHours();
            if (hours > 0) {
                durationString += hours + "h ";
            }

            long minutes = duration.toMinutes() % 60;
            if (minutes > 0) {
                durationString += minutes + "m";
            }

            return durationString;
        }
    }

    public class AgendaPlaceholderViewHolder extends RecyclerView.ViewHolder {
        private TextView text;

        public AgendaPlaceholderViewHolder(final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            text.setText("No event");
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AgendaAdapter(List<AgendaEvent> events) {
        agendaItemStore = new AgendaItemStore(events);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case VIEW_TYPE_TITLE:
                view = inflater.inflate(R.layout.agenda_day, parent, false);
                viewHolder = new AgendaTitleViewHolder(view);
                break;
            case VIEW_TYPE_EVENT:
                view = inflater.inflate(R.layout.agenda_event, parent, false);
                viewHolder = new AgendaEventViewHolder(view);
                break;
            case VIEW_TYPE_NO_EVENT:
                view = inflater.inflate(R.layout.agenda_event_placeholder, parent, false);
                viewHolder = new AgendaPlaceholderViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AgendaItem agendaItem = agendaItemStore.get(position);

        switch (getItemViewType(position)) {

            case VIEW_TYPE_TITLE:
                AgendaTitle agendaTitle = (AgendaTitle) agendaItem;
                AgendaTitleViewHolder titleViewHolder = (AgendaTitleViewHolder) holder;
                titleViewHolder.setTitle(agendaTitle.getDateTime());
                break;
            case VIEW_TYPE_EVENT:
                AgendaEvent event = (AgendaEvent) agendaItem;
                AgendaEventViewHolder eventViewHolder = (AgendaEventViewHolder) holder;
                eventViewHolder.set(event);
                break;
            case VIEW_TYPE_NO_EVENT:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return agendaItemStore.size();
    }

    @Override
    public int getItemViewType(final int position) {
        AgendaItem agendaItem = agendaItemStore.get(position);
        if (agendaItem instanceof AgendaTitle) {
            return VIEW_TYPE_TITLE;
        }
        if (agendaItem instanceof AgendaEvent) {
            return VIEW_TYPE_EVENT;
        }
        return VIEW_TYPE_NO_EVENT;
    }

    public AgendaItemStore getAgendaItemStore() {
        return agendaItemStore;
    }
}
