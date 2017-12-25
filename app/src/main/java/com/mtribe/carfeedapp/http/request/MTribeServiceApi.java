package com.mtribe.carfeedapp.http.request;

import com.mtribe.carfeedapp.http.response.CarFeeds;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sandeepn on 24-12-2017.
 */

public interface MTribeServiceApi {
    @GET("location.json")
    Call<CarFeeds> getCarFeeds();
}
