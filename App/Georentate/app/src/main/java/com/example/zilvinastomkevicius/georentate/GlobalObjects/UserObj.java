package com.example.zilvinastomkevicius.georentate.GlobalObjects;

import com.example.zilvinastomkevicius.georentate.Entities.User;

import java.util.ArrayList;

public class UserObj {

    //global variables for user management

    public static User USER;
    public static boolean IS_LOGGED;
    public static boolean REMEMBER_LOGIN;
    public static boolean EXIT_APP = false;
    public static boolean LOG_OFF = false;

    public static ArrayList<User> userArrayList = new ArrayList<>();
}
