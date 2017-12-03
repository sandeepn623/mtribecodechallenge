package com.mtribe.carfeedapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtribe.carfeedapp.http.response.model.CarFeeds;

import org.junit.Test;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * CarFeeds local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CarFeedsUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void httpTest() throws Exception {
        Request request = new Request.Builder().url("http://data.m-tribes.com/locations.json").build();
        String myResponse = null;
        CarFeeds carFeeds = null;
        OkHttpClient client = new OkHttpClient();
        try (
            Response response = client.newCall(request).execute()) {
            myResponse = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            carFeeds = objectMapper.readValue(myResponse, CarFeeds.class);
            response.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(myResponse);
        assertNotNull(carFeeds);
    }
}