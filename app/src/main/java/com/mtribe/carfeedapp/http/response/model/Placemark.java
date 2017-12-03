
package com.mtribe.carfeedapp.http.response.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "address",
    "coordinates",
    "engineType",
    "exterior",
    "fuel",
    "interior",
    "name",
    "vin"
})
public class Placemark {

    @JsonProperty("address")
    private String address;
    @JsonProperty("coordinates")
    private List<Double> coordinates = null;
    @JsonProperty("engineType")
    private String engineType;
    @JsonProperty("exterior")
    private String exterior;
    @JsonProperty("fuel")
    private Integer fuel;
    @JsonProperty("interior")
    private String interior;
    @JsonProperty("name")
    private String name;
    @JsonProperty("vin")
    private String vin;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("coordinates")
    public List<Double> getCoordinates() {
        return coordinates;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    @JsonProperty("engineType")
    public String getEngineType() {
        return engineType;
    }

    @JsonProperty("engineType")
    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    @JsonProperty("exterior")
    public String getExterior() {
        return exterior;
    }

    @JsonProperty("exterior")
    public void setExterior(String exterior) {
        this.exterior = exterior;
    }

    @JsonProperty("fuel")
    public Integer getFuel() {
        return fuel;
    }

    @JsonProperty("fuel")
    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    @JsonProperty("interior")
    public String getInterior() {
        return interior;
    }

    @JsonProperty("interior")
    public void setInterior(String interior) {
        this.interior = interior;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("vin")
    public String getVin() {
        return vin;
    }

    @JsonProperty("vin")
    public void setVin(String vin) {
        this.vin = vin;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
