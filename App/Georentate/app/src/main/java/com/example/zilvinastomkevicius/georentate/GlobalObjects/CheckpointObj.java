package com.example.zilvinastomkevicius.georentate.GlobalObjects;

import com.example.zilvinastomkevicius.georentate.Entities.AdditionalCheckInfo;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;

import java.util.ArrayList;

public class CheckpointObj {

    //Global variables for checkpoints

    public static ArrayList<Checkpoint> checkpointArrayList = new ArrayList<>();
    public static ArrayList<AdditionalCheckInfo> CLOSEST_CHECKPOINTS = new ArrayList<>();

    public static ArrayList<UserCheckpoint> userCheckpointArrayList = new ArrayList<>();

    public static ArrayList<String> newCheckpointList = new ArrayList<>();
    public static boolean NEW_CHECKPOINTS = false;
}
