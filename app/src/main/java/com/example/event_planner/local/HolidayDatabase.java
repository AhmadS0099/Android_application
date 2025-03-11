package com.example.event_planner.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.event_planner.model.Ip;

@Database(entities = {Ip.class}, version = 1, exportSchema = false)
public abstract class HolidayDatabase extends RoomDatabase {
    private static volatile HolidayDatabase INSTANCE;

    public abstract IpDao ipDao();

    public static HolidayDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HolidayDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            HolidayDatabase.class, "holiday_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}