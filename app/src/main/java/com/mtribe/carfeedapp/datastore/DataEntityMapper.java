package com.mtribe.carfeedapp.datastore;

import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;
import com.mtribe.carfeedapp.http.response.CarFeeds;
import com.mtribe.carfeedapp.http.response.Placemark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class DataEntityMapper {

    public static List<CarInformationEntity> mapResponseWithFields(CarFeeds carFeeds) {
        List<CarInformationEntity> carInformationEntityList = new ArrayList<>();
        for (Placemark placemark: carFeeds.getPlacemarks()) {
            CarInformationEntity carInformationEntity = new CarInformationEntity();
            carInformationEntity.setAddress(placemark.getAddress());
            carInformationEntity.setEngineType(placemark.getEngineType());
            carInformationEntity.setExterior(placemark.getExterior());
            carInformationEntity.setInterior(placemark.getInterior());
            carInformationEntity.setFuel(placemark.getFuel());
            carInformationEntity.setName(placemark.getName());
            carInformationEntity.setVin(placemark.getVin());
            carInformationEntity.setLongitude(placemark.getCoordinates().get(0));
            carInformationEntity.setLatitude(placemark.getCoordinates().get(1));
            carInformationEntityList.add(carInformationEntity);
        }
        return carInformationEntityList;
    }
}
