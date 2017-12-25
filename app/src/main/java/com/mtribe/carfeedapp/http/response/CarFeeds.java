
package com.mtribe.carfeedapp.http.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class CarFeeds {
    @SerializedName("placemarks")
    @Expose
    private List<Placemark> placemarks = null;

    public List<Placemark> getPlacemarks() {
        return placemarks;
    }

    public void setPlacemarks(List<Placemark> placemarks) {
        this.placemarks = placemarks;
    }
}
