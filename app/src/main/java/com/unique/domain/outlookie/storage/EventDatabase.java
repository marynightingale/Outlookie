package com.unique.domain.outlookie.storage;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class EventDatabase extends RoomDatabase {
    public abstract EventDao daoAccess();

    private static EventDatabase INSTANCE;

    public static EventDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventDatabase.class, "event_database")
                            .addCallback(roomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDatabaseAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {

        private final EventDao eventDao;

        PopulateDatabaseAsync(EventDatabase db) {
            eventDao = db.daoAccess();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            eventDao.deleteAll();

            List<Event> events = new ArrayList<>();

            events.add(new Event(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), "Team Meeting", "desc", "Golden Gate", Color.parseColor("#AD7A99")));
            events.add(new Event(LocalDateTime.now().minusHours(1), LocalDateTime.now(), "New cool feature kick-off", "desc", null, Color.parseColor("#4E937A")));
            events.add(new Event(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "1:1 with Maria", "desc", "Blue Bottle", Color.parseColor("#414770")));
            events.add(new Event(LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(1).plusMinutes(30), "Weekly for new feature", "desc", null, Color.parseColor("#414770")));
            events.add(new Event(LocalDateTime.now().plusDays(6), LocalDateTime.now().plusDays(4).plusHours(1).plusMinutes(30), "OTG townhall", "desc", null, Color.parseColor("#4E937A")));
            events.add(new Event(LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(4).plusHours(1).plusMinutes(30), "Q&A with Satya", "desc", null, Color.parseColor("#559CAD")));
            events.add(new Event(LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(4).plusHours(1).plusMinutes(30), "1:1 with Jimmy", "desc", null, Color.parseColor("#559CAD")));

            eventDao.insertEvents(events);
            return null;
        }
    }

}