package com.unique.domain.outlookie.storage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insertEvent(Event event);

    @Insert
    void insertEvents(List<Event> events);

//    @Query("SELECT * FROM Event WHERE movieId = :movieId")
//    Event fetchOneMoviesbyMovieId (int movieId);

    @Query("SELECT * FROM event")
    LiveData<List<Event>> getAllEvents();

    @Update
    void updateMovie (Event event);

    @Delete
    void deleteMovie (Event event);

    @Query("DELETE FROM event")
    void deleteAll();
}
