package com.example.zilvinastomkevicius.georentate.APIClients;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.Activites.LoginSignUpActivity;
import com.example.zilvinastomkevicius.georentate.Activites.MainActivity;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.example.zilvinastomkevicius.georentate.Fragments.LoginFragment;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginSignUpClients {

    private static final int RESPONSE_CODE_OK = 200;
    private static final int RESPONSE_CODE_INTERNAL_SERVER_ERROR = 500;
    private static final int RESPONSE_CODE_BAD_GATEWAY = 502;

    private Context mContext;
    private boolean swith = false;
    private CheckpointClients checkpointClients;

    public LoginSignUpClients(Context context) {
        mContext = context;
        checkpointClients = new CheckpointClients(mContext);
    }

    //---------------------------------- Clients -------------------------------------------

    public void logOn(final User user, final ProgressBar progressBar, final Button button) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<User> call = retrofitFactoryInterface.logOn(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                loginHandling(response, button, progressBar);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                if(!swith) {
                    button.setText("Login");
                    progressBar.setVisibility(View.INVISIBLE);
                }

                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUser(final User user, final boolean loginAfter, final ProgressBar progressBar, final Button button) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Integer> call = retrofitFactoryInterface.addUser(user);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                addUserHandling(response, progressBar, button, user, loginAfter);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                button.setText("Sign up");
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //--------------------------------- Response handling ------------------------------------------

    public void loginHandling(Response<User> response, Button button, ProgressBar progressBar) {
        if(response.code() == RESPONSE_CODE_OK) {
            User user1 = response.body();
            if(user1 != null) {
                if(user1.Login != null && user1.Password != null) {
                    UserObj.USER = new User();
                    UserObj.USER = user1;
                    UserObj.IS_LOGGED = true;

                    checkpointClients.getUserCheckpoints(UserObj.USER.ID, progressBar, button, swith);
                }

                else {
                    Toast.makeText(mContext, "Wrong email or password", Toast.LENGTH_SHORT).show();
                    button.setText("Login");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            else {
                Toast.makeText(mContext, "Bad request", Toast.LENGTH_SHORT).show();
                button.setText("Login");
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            Toast.makeText(mContext, "Something wrong inside the server", Toast.LENGTH_SHORT).show();
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            Toast.makeText(mContext, "No connection to the server", Toast.LENGTH_SHORT).show();
        }
    }

    public void addUserHandling(Response<Integer> response, ProgressBar progressBar, Button button, User user, boolean loginAfter) {
        if(response.code() == RESPONSE_CODE_OK) {
            checkpointClients.addUserCheckpoints(setUpUserCheckpoints(response.body()), progressBar, button);

            if(loginAfter) {
                swith = true;

                User user1 = new User();
                user1.Login = user.Login;
                user1.Password = user.Password;

                logOn(user1, progressBar, button);
            }
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            Toast.makeText(mContext, "The user already exists", Toast.LENGTH_SHORT).show();
            button.setText("Sign up");
            progressBar.setVisibility(View.INVISIBLE);
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            Toast.makeText(mContext, "No connection to the server", Toast.LENGTH_SHORT).show();
            button.setText("Sign up");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public ArrayList<UserCheckpoint> setUpUserCheckpoints(int id) {
        ArrayList<UserCheckpoint> userCheckpoints = new ArrayList<>();

        for(Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
            UserCheckpoint userCheckpoint = new UserCheckpoint();
            userCheckpoint.UserID = id;
            userCheckpoint.CheckpointID = checkpoint.ID;
            userCheckpoint.Completed = false;

            userCheckpoints.add(userCheckpoint);
        }

        return userCheckpoints;
    }
}
