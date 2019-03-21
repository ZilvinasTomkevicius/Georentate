package com.example.zilvinastomkevicius.georentate.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * User entity
 */
public class User {

    @JsonProperty("id")
    private int id;
    @JsonProperty("login")
    private String login;
    @JsonProperty("password")
    private String password;
    @JsonProperty("email")
    private String email;
    @JsonProperty("registerDate")
    private String registerDate;
    @JsonProperty("points")
    private float points;

    public User() {
    }

    public User(int id, String login, String password, String email, String registerDate, float points) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
        this.points = points;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
    public void setPoints(float points) {
        this.points = points;
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRegisterDate() {
        return registerDate;
    }
    public float getPoints() {
        return points;
    }
}
