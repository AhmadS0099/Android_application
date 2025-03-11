package com.example.event_planner.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

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

    // Constructor
    public Ip(@NonNull String date, String localName, String name) {
        this.date = date;
        this.localName = localName;
        this.name = name;
    }

    // Getters
    public String getDate() { return date; }
    public String getLocalName() { return localName; }
    public String getName() { return name; }
}