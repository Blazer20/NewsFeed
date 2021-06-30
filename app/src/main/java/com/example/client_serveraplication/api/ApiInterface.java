package com.example.client_serveraplication.api;

import com.example.client_serveraplication.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getBusinessNews(
        @Query("country") String country,
        @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<News> getAppleNews(
            @Query("q") String q,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<News> getBitcoinNews(
            @Query("q") String q,
            @Query("apiKey") String apiKey
    );
}
