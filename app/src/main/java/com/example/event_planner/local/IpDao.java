package com.example.event_planner.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.event_planner.model.Ip;
import java.util.List;

@Dao
public interface IpDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHolidays(List<Ip> holidays);

    @Insert
    void insertCustomHoliday(Ip holiday);

    @Query("SELECT * FROM holidays ORDER BY date ASC")
    List<Ip> getAllHolidays();

    @Query("DELETE FROM holidays")
    void deleteAllHolidays();

    // In IpDao.java
    @Query("SELECT * FROM holidays WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    List<Ip> getEventsBetweenDates(String startDate, String endDate);
}