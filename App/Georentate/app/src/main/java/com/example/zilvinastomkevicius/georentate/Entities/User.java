package com.example.zilvinastomkevicius.georentate.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class User {

    @JsonProperty("ID")
    public  int ID;
    @JsonProperty("Login")
    public String Login;
    @JsonProperty("Password")
    public String Password;
    @JsonProperty("Email")
    public String Email;
    @JsonProperty("RegisterDate")
    public String RegisterDate;
    @JsonProperty("Points")
    public int Points;
}
