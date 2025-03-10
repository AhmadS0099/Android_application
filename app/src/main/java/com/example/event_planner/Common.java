package com.example.event_planner;

import com.example.event_planner.Remote.IpService;
import com.example.event_planner.Remote.RetrofitClient;

public class Common {
    private static final String BASE_URL = "https://date.nager.at/api/v3/";

    public static IpService getIpService() {
        return RetrofitClient.getClient(BASE_URL).create(IpService.class);
    }
}