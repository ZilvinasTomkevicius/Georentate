package com.example.zilvinastomkevicius.georentate.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Checkpoint entity
 */

public class Checkpoint {

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("scan")
    private String scan;
    @JsonProperty("hint")
    private String hint;
    @JsonProperty("latitude")
    private double latitude;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("points")
    private float points;

    private int ExactDistance;
    private String ApproximateDistance;
    private String TravelTime;

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setScan(String scan) {
        this.scan = scan;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setPoints(float points) {
        this.points = points;
    }

    public void setExactDistance(int exactDistance) {
        this.ExactDistance = exactDistance;
    }
    public void setApproximateDistance(String approximateDistance) {
        this.ApproximateDistance = approximateDistance;
    }
    public void setTravelTime(String travelTime) {
        this.TravelTime = travelTime;
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getScan() {
        return scan;
    }
    public String getHint() {
        return hint;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public float getPoints() {
        return points;
    }

    public int getExactDistance() {
        return ExactDistance;
    }
    public String getApproximateDistance() {
        return ApproximateDistance;
    }
    public String getTravelTime() {
        return TravelTime;
    }
}
