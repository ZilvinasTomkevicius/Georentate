package com.example.zilvinastomkevicius.georentate.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Checkpoint {

    @JsonProperty("ID")
    public int ID;
    @JsonProperty("Name")
    public String Name;
    @JsonProperty("Scan")
    public String Scan;
    @JsonProperty("Hint")
    public String Hint;
    @JsonProperty("Latitude")
    public double Latitude;
    @JsonProperty("Longitude")
    public double Longitude;
    @JsonProperty("Points")
    public int Points;
}
