package com.example.zilvinastomkevicius.georentate.APIClients;

import com.example.zilvinastomkevicius.georentate.Entities.AppInfo;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface for requesting data from API
 */

public interface RetrofitFactoryInterface {

    String BASE_URL = "https://api-dt3.conveyor.cloud/";

    //USER
    @POST("api/user/add")
    Call<Integer> addUser(@Body User user);
    @PUT("api/user/update")
    Call<Void> updateUser(@Body User user);
    @GET("api/user/delete/{id}")
    Call<Void> deleteUser(@Path("id") int id);
    @GET("api/user/get/{id}")
    Call<JsonObject> getUser(@Path("id") int id);
    @GET("api/user/getlist")
    Call<ArrayList<JsonObject>> getUserList();

    @POST("api/user/logOn")
    Call<User> logOn(@Body User user);

    //CHECKPOINT
    @POST("api/checkpoint/add")
    Call<Void> addCheckpoint(@Body Checkpoint checkpoint);
    @PUT("api/checkpoint/update")
    Call<Void> updateCheckpoint(@Body Checkpoint checkpoint);
    @GET("api/checkpoint/delete/{id}")
    Call<Void> deleteCheckpoint(@Path("id") int id);
    @GET("api/checkpoint/get/{id}")
    Call<JsonObject> getCheckpoint(@Path("id") int id);
    @GET("api/checkpoint/get")
    Call<ArrayList<JsonObject>> getCheckpointList();

    //USER CHECKPOINT
    @POST("api/userCheckpoint/add")
    Call<Void> addUserCheckpoints(@Body ArrayList<UserCheckpoint> userCheckpoints);
    @GET("api/userCheckpoint/getlist/{id}")
    Call<ArrayList<JsonObject>> getUserCheckpoints(@Path("id") int id);
    @PUT("api/userCheckpoint/update")
    Call<Void> updateUserCheckpoint(@Body UserCheckpoint userCheckpoint);
    @PUT("api/userCheckpoint/updateList")
    Call<Void> updateUserCheckpointList(@Body ArrayList<UserCheckpoint> userCheckpointList);

    //APP INFO
    @POST("api/appInfo/add")
    Call<Void> addAppInfo(@Body AppInfo appInfo);
    @GET("api/appInfo/get")
    Call<JsonObject> getAppInfo();
    @PUT("api/appInfo/update")
    Call<Void> updateAppInfo(@Body AppInfo appInfo);
}
