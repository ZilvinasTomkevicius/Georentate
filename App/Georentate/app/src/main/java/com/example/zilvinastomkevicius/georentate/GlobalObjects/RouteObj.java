package com.example.zilvinastomkevicius.georentate.GlobalObjects;

import java.util.ArrayList;

public class RouteObj {

    //global variables for route management

    public static String BASE_URI = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    public static String DESTINATION_URI = "&destination=";
    public static String KEY_URI = "&key=AIzaSyBXBO4a2JeKmY8_Q1UxerES4d_HV2UBjVk";

    //
    public static ArrayList<String> ROUTE = new ArrayList<>();
    public static boolean ROUTE_FOUND = false;
    public static String ROUTE_DISTANCE;
    public static String ROUTE_DURATION;
}
