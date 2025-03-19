package com.example.event_planner.Remote;

import com.example.event_planner.model.Ip;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit service interface for fetching holiday data from the API.
 */
public interface IpService {

    /**
     * Retrieves a list of public holidays for 2025 in the Netherlands.
     *
     * @return A Call object for the HTTP request.
     */
    @GET("PublicHolidays/2025/NL")
    Call<List<Ip>> getHolidays();
}