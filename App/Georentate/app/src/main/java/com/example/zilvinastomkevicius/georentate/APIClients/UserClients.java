package com.example.zilvinastomkevicius.georentate.APIClients;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.Activites.AdminActivity;
import com.example.zilvinastomkevicius.georentate.Activites.LaunchingActivity;
import com.example.zilvinastomkevicius.georentate.Activites.LoginSignUpActivity;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserClients {

    private static final int RESPONSE_CODE_OK = 200;
    private static final int RESPONSE_CODE_INTERNAL_SERVER_ERROR = 500;
    private static final int RESPONSE_CODE_BAD_GATEWAY = 502;

    private Context mContext;

    public UserClients(Context context) {
        mContext = context;
    }

    public void getUserList() {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<ArrayList<User>> call = retrofitFactoryInterface.getUserList();

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                getUserListHandling(response);
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addNewUserCheckpoints(ArrayList<UserCheckpoint> userCheckpoints) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.addUserCheckpoints(userCheckpoints);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                addNewUserCheckpointsHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUser(User user) {
        updateUserWindow();
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.updateUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                updateUserHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //====================================== Handling ================================

    public void updateUserHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            updateUserFinished("User info updated!");
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            updateUserFinished("The login already exists");
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            updateUserFinished("Server went down :(");
        }
    }

    public void getUserListHandling(Response<ArrayList<User>> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            UserObj.userArrayList = response.body();

            mContext.startActivity(new Intent(mContext, LoginSignUpActivity.class));
            ((LaunchingActivity)mContext).finish();
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            updateUserFinished("Something went wrong.");
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            updateUserFinished("Server went down :(");
        }
    }

    public void addNewUserCheckpointsHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            updateUserFinished("All players updated!");

            CheckpointObj.NEW_CHECKPOINTS = false;
            CheckpointObj.userCheckpointArrayList.clear();
            CheckpointObj.newCheckpointList.clear();

            if(UserObj.LOG_OFF) {
                mContext.startActivity(new Intent(mContext, LaunchingActivity.class));
                ((AdminActivity)mContext).finish();
                UserObj.LOG_OFF = false;
            }
            else if(UserObj.EXIT_APP) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                UserObj.EXIT_APP = false;
            }
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            updateUserFinished("Something went wrong.");
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            updateUserFinished("Server went down :(");
        }
    }

    public void updateUserWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Updating info..");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateUserFinished(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
