package com.example.event_planner.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.event_planner.model.Ip;

/**
 * Singleton class representing the Room database for holiday data.
 */
@Database(entities = {Ip.class}, version = 1, exportSchema = false)
public abstract class HolidayDatabase extends RoomDatabase {
    private static volatile HolidayDatabase INSTANCE;

    /**
     * Provides access to the IpDao for database operations.
     *
     * @return The IpDao instance.
     */
    public abstract IpDao ipDao();

    /**
     * Retrieves the singleton instance of HolidayDatabase.
     *
     * @param context The application context.
     * @return The singleton instance of HolidayDatabase.
     */
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

    /**
     * Deletes the holiday database.
     *
     * @param context The application context.
     */
    public static void deleteDatabase(Context context) {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
        context.deleteDatabase("holiday_database");
    }
}