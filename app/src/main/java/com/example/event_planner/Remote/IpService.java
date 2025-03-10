package com.example.event_planner.Remote;

import com.example.event_planner.model.Ip;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IpService {
    @GET("PublicHolidays/2025/NL")
    Call<List<Ip>> getHolidays();
}