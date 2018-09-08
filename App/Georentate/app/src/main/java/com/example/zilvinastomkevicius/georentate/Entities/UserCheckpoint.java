package com.example.zilvinastomkevicius.georentate.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCheckpoint {

    @JsonProperty("UserID")
    public int UserID;
    @JsonProperty("CheckpointID")
    public int CheckpointID;
    @JsonProperty("Completed")
    public boolean Completed;
}
