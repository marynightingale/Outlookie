package com.unique.domain.outlookie.storage;

import android.arch.persistence.room.TypeConverter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class Converters {

    static ZoneOffset zoneOffset = ZoneOffset.UTC; // needs to be set from phone settings

    @TypeConverter
    public static LocalDateTime fromTimestamp(Long value) {
        return value == null ? null : LocalDateTime.ofEpochSecond(value, 0, zoneOffset);
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toEpochSecond(zoneOffset);
    }
}
