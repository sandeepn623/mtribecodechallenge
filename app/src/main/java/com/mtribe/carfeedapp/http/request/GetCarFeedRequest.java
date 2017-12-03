package com.mtribe.carfeedapp.http.request;

/**
 * Created by Sandeepn on 02-12-2017.
 */
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtribe.carfeedapp.http.response.model.CarFeeds;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetCarFeedRequest {

    private Callback mCallback;
    private static final String TAG = "GetCarFeedRequest";
    public CarFeeds sendRequest(String url) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        CarFeeds carFeeds = null;
        try {
            response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                carFeeds = processJsonResponse(response);
            } else {
                Log.d(TAG, "response status: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(response != null)
                response.close();
        }
        return carFeeds;
    }

    public CarFeeds processJsonResponse(Response response) throws IOException{
        String myResponse = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(myResponse, CarFeeds.class);
    }
}
