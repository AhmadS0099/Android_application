package com.example.event_planner.model;

import com.google.gson.annotations.SerializedName;

public class Ip {
    @SerializedName("date")
    private String date;

    @SerializedName("localName")
    private String localName;

    @SerializedName("name")
    private String name;

    // Getters
    public String getDate() { return date; }
    public String getLocalName() { return localName; }
    public String getName() { return name; }
}