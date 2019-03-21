package com.example.zilvinastomkevicius.georentate.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserCheckpoint entity
 */
public class UserCheckpoint {

    @JsonProperty("UserID")
    private int UserID;
    @JsonProperty("CheckpointID")
    private int CheckpointID;
    @JsonProperty("Completed")
    private boolean Completed;

    //Setters
    public void setUserID(int userID) {
        UserID = userID;
    }
    public void setCheckpointID(int checkpointID) {
        CheckpointID = checkpointID;
    }
    public void setCompleted(boolean completed) {
        Completed = completed;
    }

    //Getters
    public int getUserID() {
        return UserID;
    }
    public int getCheckpointID() {
        return CheckpointID;
    }
    public boolean isCompleted() {
        return Completed;
    }
}
