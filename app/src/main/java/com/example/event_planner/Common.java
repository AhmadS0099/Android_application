package com.example.event_planner;

import com.example.event_planner.Remote.IpService;
import com.example.event_planner.Remote.RetrofitClient;

/**
 * Utility class providing common configurations and services.
 */
public class Common {
    private static final String BASE_URL = "https://date.nager.at/api/v3/";

    /**
     * Retrieves an instance of IpService for API interactions.
     *
     * @return The IpService instance.
     */
    public static IpService getIpService() {
        return RetrofitClient.getClient(BASE_URL).create(IpService.class);
    }
}