package com.mtribe.carfeedapp.datastore.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.mtribe.carfeedapp.model.CarInformation;

/**
 * Created by Sandeepn on 02-12-2017.
 */
@Entity(tableName = "carfeeds")
public class CarInformationEntity implements CarInformation{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "engineType")
    private String engineType;
    @ColumnInfo(name = "exterior")
    private String exterior;
    @ColumnInfo(name = "fuel")
    private Integer fuel;
    @ColumnInfo(name = "interior")
    private String interior;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "vin")
    private String vin;

    public CarInformationEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getExterior() {
        return exterior;
    }

    public void setExterior(String exterior) {
        this.exterior = exterior;
    }

    public Integer getFuel() {
        return fuel;
    }

    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) { this.longitude = longitude; }

}
