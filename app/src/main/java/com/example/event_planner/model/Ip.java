package com.example.event_planner.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 *  holiday entity with date, local name, and global name.
 */
@Entity(tableName = "holidays")
public class Ip {
    @PrimaryKey
    @NonNull
    @SerializedName("date")
    private String date;

    @SerializedName("localName")
    private String localName;

    @SerializedName("name")
    private String name;

    /**
     * Constructs an Ip instance with specified date, local name, and global name.
     *
     * @param date      The date of the holiday.
     * @param localName The local name of the holiday.
     * @param name      The global name of the holiday.
     */
    public Ip(@NonNull String date, String localName, String name) {
        this.date = date;
        this.localName = localName;
        this.name = name;
    }

    /**
     * Gets the date of the holiday.
     *
     * @return The date of the holiday.
     */
    public String getDate() { return date; }
    /**
     * Gets the local name of the holiday.
     *
     * @return The local name of the holiday.
     */
    public String getLocalName() { return localName; }
    /**
     * Gets the global name of the holiday.
     *
     * @return The global name of the holiday.
     */
    public String getName() { return name; }
}