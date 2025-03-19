package com.example.event_planner.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class for managing the Retrofit client instance.
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;

    /**
     * Retrieves the Retrofit client instance with the specified base URL.
     *
     * @param baseUrl The base URL for the API.
     * @return The Retrofit client instance.
     */
    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}