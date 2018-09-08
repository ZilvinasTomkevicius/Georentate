package com.example.zilvinastomkevicius.georentate.APIClients;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.Activites.LaunchingActivity;
import com.example.zilvinastomkevicius.georentate.Activites.LoginSignUpActivity;
import com.example.zilvinastomkevicius.georentate.Activites.MainActivity;
import com.example.zilvinastomkevicius.georentate.Adapters.AddDialogClass;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.example.zilvinastomkevicius.georentate.Fragments.MapFragment;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.RouteObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckpointClients {

    private static final int RESPONSE_CODE_OK = 200;
    private static final int RESPONSE_CODE_INTERNAL_SERVER_ERROR = 500;
    private static final int RESPONSE_CODE_BAD_GATEWAY = 502;

    private Context mContext;

    public CheckpointClients(Context context) {
        mContext = context;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.addCheckpoint(checkpoint);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                addCheckpointHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCheckpoint(Checkpoint checkpoint) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.updateCheckpoint(checkpoint);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                updateCheckpointHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteCheckpoint(int Id) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.deleteCheckpoint(Id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                deleteCheckpointHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCheckpointList() {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<ArrayList<Checkpoint>> call = retrofitFactoryInterface.getCheckpointList();

        call.enqueue(new Callback<ArrayList<Checkpoint>>() {
            @Override
            public void onResponse(Call<ArrayList<Checkpoint>> call, Response<ArrayList<Checkpoint>> response) {
                getCheckpointHandling(response);
            }

            @Override
            public void onFailure(Call<ArrayList<Checkpoint>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserCheckpoints(ArrayList<UserCheckpoint> userCheckpoints, final ProgressBar progressBar, final Button button) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.addUserCheckpoints(userCheckpoints);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                addUserCheckpointsHandling(response, progressBar, button);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserCheckpoints(int id, final ProgressBar progressBar, final Button button, final boolean swith) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<ArrayList<UserCheckpoint>> call = retrofitFactoryInterface.getUserCheckpoints(id);

        call.enqueue(new Callback<ArrayList<UserCheckpoint>>() {
            @Override
            public void onResponse(Call<ArrayList<UserCheckpoint>> call, Response<ArrayList<UserCheckpoint>> response) {
                getUserCheckpointsHandling(response, progressBar, button, swith);
            }

            @Override
            public void onFailure(Call<ArrayList<UserCheckpoint>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserCheckpoint(UserCheckpoint userCheckpoint) {
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.updateUserCheckpoint(userCheckpoint);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                updateUserCheckpointHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //======================================== Handling ================================

    public void addCheckpointHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            checkpointAlert("New checkpoint has been added!");
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            checkpointAlert("something went wrong");
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            checkpointAlert("No connection to the server");
        }
    }

    public void updateCheckpointHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            checkpointAlert("The checkpoint has been edited!");
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            checkpointAlert("something went wrong");
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            checkpointAlert("No connection to the server");
        }
    }

    public void deleteCheckpointHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            checkpointAlert("The checkpoint has been deleted.");
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            checkpointAlert("something went wrong");
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            checkpointAlert("No connection to the server");
        }
    }

    public void getCheckpointHandling(Response<ArrayList<Checkpoint>> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            CheckpointObj.checkpointArrayList = response.body();

            if(CheckpointObj.NEW_CHECKPOINTS) {
                updateUserCheckpoints();
            }
            else {
                UserClients userClients = new UserClients(mContext);
                userClients.getUserList();
            }
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            noConnectionAlert();
        }
    }

    public void updateUserCheckpoints() {

        ArrayList<Integer> newCheckpointIDs = new ArrayList<>();

        for(String name : CheckpointObj.newCheckpointList) {
            for(Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
                if(name.equals(checkpoint.Name)) {
                    newCheckpointIDs.add(checkpoint.ID);
                    break;
                }
            }
        }
        for(Integer id : newCheckpointIDs) {
            for(User user : UserObj.userArrayList) {
                UserCheckpoint userCheckpoint = new UserCheckpoint();
                userCheckpoint.CheckpointID = id;
                userCheckpoint.UserID = user.ID;
                userCheckpoint.Completed = false;
                CheckpointObj.userCheckpointArrayList.add(userCheckpoint);
            }
        }

        UserClients userClients = new UserClients(mContext);
        userClients.addNewUserCheckpoints(CheckpointObj.userCheckpointArrayList);
    }

    public void addUserCheckpointsHandling(Response<Void> response, ProgressBar progressBar, Button button) {
        if(response.code() == RESPONSE_CODE_OK) {
            Toast.makeText(mContext, "Succesfuly signed up!", Toast.LENGTH_SHORT).show();

            button.setText("Sign up");
            progressBar.setVisibility(View.INVISIBLE);
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            Toast.makeText(mContext, "No connection to the server", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserCheckpointsHandling(Response<ArrayList<UserCheckpoint>> response, ProgressBar progressBar, Button button, boolean swith) {
        if(response.code() == RESPONSE_CODE_OK) {
            CheckpointObj.userCheckpointArrayList = response.body();

            if(!swith) {
                button.setText("Login");
                progressBar.setVisibility(View.INVISIBLE);
            }

            mContext.startActivity(new Intent(mContext, MainActivity.class));
            ((LoginSignUpActivity)mContext).finish();
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            Toast.makeText(mContext, "No connection to the server", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUserCheckpointHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            Toast.makeText(mContext, "User checkpoint updated!", Toast.LENGTH_SHORT).show();
        }

        else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
        }

        else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            Toast.makeText(mContext, "No connection to the server", Toast.LENGTH_SHORT).show();
        }
    }

    public void noConnectionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Application server is currently down :(");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void checkpointAlert(String message) {
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
}
