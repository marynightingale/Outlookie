package com.unique.domain.outlookie.storage;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private EventDao eventDao;
    private LiveData<List<Event>> events;

    public EventViewModel(Application application) {
        super(application);
        EventDatabase db = EventDatabase.getDatabase(application);
        eventDao = db.daoAccess();
        events = eventDao.getAllEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }


    public void insert(Event event) {
        new insertAsyncTask(eventDao).execute(event);
    }

    private static class insertAsyncTask extends AsyncTask<Event, Void, Void> {

        private EventDao mAsyncTaskDao;

        insertAsyncTask(EventDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.insertEvent(params[0]);
            return null;
        }
    }
}
