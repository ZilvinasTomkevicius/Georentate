package com.example.zilvinastomkevicius.georentate.Update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.APIClients.RetrofitFactoryClass;
import com.example.zilvinastomkevicius.georentate.APIClients.RetrofitFactoryInterface;
import com.example.zilvinastomkevicius.georentate.APIClients.UserClients;
import com.example.zilvinastomkevicius.georentate.Activites.LaunchingActivity;
import com.example.zilvinastomkevicius.georentate.Activites.LoginSignUpActivity;
import com.example.zilvinastomkevicius.georentate.Entities.AppInfo;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.AppInfoObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.JsonObjectParsing.CheckpointParsing;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateGeneralData {

    private static final int RESPONSE_CODE_OK = 200;
    private static final int RESPONSE_CODE_INTERNAL_SERVER_ERROR = 500;
    private static final int RESPONSE_CODE_BAD_GATEWAY = 502;

    private Context mContext;

    public UpdateGeneralData(Context context) {
        mContext = context;
    }

    //FOR UPDATING GENERAL APP INFO
    public void getAppInfo() {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<JsonObject> call = retrofitFactoryInterface.getAppInfo();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                getInfoHandling(response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getInfoHandling(Response<JsonObject> response) {
        if(response.code() == RESPONSE_CODE_OK) {

            AppInfoObj.APP_INFO.Version = "1.0";
            toastMessage("Updated!");
        } else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            toastMessage("Something went wrong.");
        } else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            toastMessage("No connection to the server.");
        }
    }

    //FOR UPDATING GENERAL CHECKPONTS
    public void getCheckpointList() {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<ArrayList<JsonObject>> call = retrofitFactoryInterface.getCheckpointList();

        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                getCheckpointHandling(response);
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCheckpointHandling(Response<ArrayList<JsonObject>> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            CheckpointParsing.parseArrayList(response.body());
            toastMessage("Updated!");
        } else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
        } else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            Toast.makeText(mContext, "bad gateway", Toast.LENGTH_SHORT).show();
        }
    }

    //FOR UPDATING USER CHECKPOINTS
    public void updateUserCheckpoints(ArrayList<UserCheckpoint> userCheckpointList) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.updateUserCheckpointList(userCheckpointList);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                updateUserCheckpointsHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserCheckpointsHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            toastMessage("Updated!");
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            Toast.makeText(mContext, "bad gateway", Toast.LENGTH_SHORT).show();
        }
    }

    //========================= Alerts ======================

    public void appInfoAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
