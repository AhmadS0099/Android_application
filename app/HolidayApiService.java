package com.example.event_planner.HolidayApiService;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface HolidayApiService {
    @GET("api/v2/PublicHolidays/2024/NL") // Lowercase "api"
    Call<List<Holiday>> getHolidays();
}
