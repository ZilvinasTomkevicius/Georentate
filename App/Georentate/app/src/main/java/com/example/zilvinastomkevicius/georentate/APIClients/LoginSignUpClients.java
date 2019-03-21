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

/**
 *A class for login/signup API
 */

public class LoginSignUpClients {

    private static final int RESPONSE_CODE_OK = 200;
    private static final int RESPONSE_CODE_INTERNAL_SERVER_ERROR = 500;
    private static final int RESPONSE_CODE_BAD_GATEWAY = 502;

    private Context mContext;
    private boolean swith = false;
    private CheckpointClients checkpointClients;
    private AppInfoClients appInfoClients;

    public LoginSignUpClients(Context context) {
        mContext = context;
        checkpointClients = new CheckpointClients(mContext);
        appInfoClients = new AppInfoClients(mContext);
    }

    /**
     * Clients
     * @param user
     * @param progressBar
     * @param button
     */

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
                message("Error: ");
            }
        });
    }

    public void addUser(final User user, final ProgressBar progressBar, final Button button) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Integer> call = retrofitFactoryInterface.addUser(user);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                addUserHandling(response, progressBar, button, user);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                button.setText("Sign up");
                progressBar.setVisibility(View.INVISIBLE);

                message("Error: ");
            }
        });
    }

    /**
     * Response handling
     * @param response
     * @param button
     * @param progressBar
     */

    public void loginHandling(Response<User> response, Button button, ProgressBar progressBar) {
        if(response.code() == RESPONSE_CODE_OK) {
            User user1 = new User(response.body().getId(), response.body().getLogin(), response.body().getPassword(), response.body().getEmail(),
                    response.body().getRegisterDate(), response.body().getPoints());

            if(user1 != null) {
                if(user1.getLogin() != null && user1.getPassword() != null) {
                    UserObj.USER = new User();
                    UserObj.USER = user1;
                    UserObj.IS_LOGGED = true;

                    checkpointClients.getUserCheckpoints(UserObj.USER.getId(), progressBar, button, swith);
                } else {
                    message("Wrong email or password");
                    button.setText("Login");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            } else {
                message("Bad request");
                button.setText("Login");
                progressBar.setVisibility(View.INVISIBLE);
            }
        } else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            message("Something wrong inside the server");
        } else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            message("No connection to the server");
        }
    }

    public void addUserHandling(Response<Integer> response, ProgressBar progressBar, Button button, User user) {
        if(response.code() == RESPONSE_CODE_OK) {
            User user1 = new User();
            user1.setLogin(user.getLogin());
            user1.setPassword(user.getPassword());
            user1.setPoints(0);

            checkpointClients.addUserCheckpoints(setUpUserCheckpoints(response.body()), progressBar, button, user1);

            if(CheckpointObj.checkpointArrayList.size() == 0) {
                checkpointClients.getCheckpointList();
            }
        } else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            message("The user already exists");
            button.setText("Sign up");
            progressBar.setVisibility(View.INVISIBLE);
        } else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            message("No connection to the server");
            button.setText("Sign up");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public ArrayList<UserCheckpoint> setUpUserCheckpoints(int id) {
        ArrayList<UserCheckpoint> userCheckpoints = new ArrayList<>();

        for(Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
            UserCheckpoint userCheckpoint = new UserCheckpoint();
            userCheckpoint.setUserID(id);
            userCheckpoint.setCheckpointID(checkpoint.getId());
            userCheckpoint.setCompleted(false);

            userCheckpoints.add(userCheckpoint);
        }
        return userCheckpoints;
    }

    public void message(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
