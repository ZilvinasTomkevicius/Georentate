package com.example.zilvinastomkevicius.georentate.JsonObjectParsing;

import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Response;

public class CheckpointParsing {

    public static void parseArrayList(ArrayList<JsonObject> jsonObjectArrayList) {
        for(JsonObject jsonObject : jsonObjectArrayList) {
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setId(jsonObject.get("ID").getAsInt());
            checkpoint.setName(jsonObject.get("Name").getAsString());
            checkpoint.setScan(jsonObject.get("Scan").getAsString());
            checkpoint.setHint(jsonObject.get("Hint").getAsString());
            checkpoint.setLatitude(jsonObject.get("Latitute").getAsDouble());
            checkpoint.setLongitude(jsonObject.get("Longitude").getAsDouble());
            checkpoint.setPoints(jsonObject.get("Points").getAsFloat());

            CheckpointObj.checkpointArrayList.add(checkpoint);
        }
    }
}
