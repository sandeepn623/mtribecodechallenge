package com.mtribe.carfeedapp.http.response;

/**
 * Created by Sandeepn on 02-12-2017.
 */
/*public class GetCarFeedResponse implements Callback{
    private static final String TAG = "GetCarFeedResponse";

    public void processJsonResponse(Response response) throws IOException{
        String myResponse = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        CarFeeds carFeeds = objectMapper.readValue(myResponse, CarFeeds.class);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        call.cancel();
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        boolean isSuccessful = response.isSuccessful();
        Log.d(TAG, "http response successful: " + isSuccessful);
        if(isSuccessful) {
            processJsonResponse(response);
            response.close();
        }else {
            throw new IOException();
        }
    }
}*/
