package com.example.event_planner.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.event_planner.model.Ip;
import java.util.List;

/**
 * Data Access Object (DAO) for accessing holiday data in the database.
 */
@Dao
public interface IpDao {

    /**
     * Inserts or replaces a list of holidays into the database.
     *
     * @param holidays The list of holidays to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHolidays(List<Ip> holidays);

    /**
     * Inserts a custom holiday into the database.
     *
     * @param holiday The holiday to insert.
     */
    @Insert
    void insertCustomHoliday(Ip holiday);

    /**
     * Retrieves all holidays from the database, ordered by date in ascending order.
     *
     * @return A list of all holidays.
     */
    @Query("SELECT * FROM holidays ORDER BY date ASC")
    List<Ip> getAllHolidays();

    /**
     * Deletes all holidays from the database.
     */
    @Query("DELETE FROM holidays")
    void deleteAllHolidays();

    /**
     * gets holidays occurring between the specified start and end dates.
     *
     * @param startDate The start date in ISO format (yyyy-MM-dd).
     * @param endDate   The end date in ISO format (yyyy-MM-dd).
     * @return A list of holidays between the specified dates.
     */
    @Query("SELECT * FROM holidays WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    List<Ip> getEventsBetweenDates(String startDate, String endDate);
}