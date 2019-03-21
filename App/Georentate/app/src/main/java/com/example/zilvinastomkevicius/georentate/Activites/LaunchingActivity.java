package com.example.zilvinastomkevicius.georentate.Activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zilvinastomkevicius.georentate.APIClients.AppInfoClients;
import com.example.zilvinastomkevicius.georentate.APIClients.CheckpointClients;
import com.example.zilvinastomkevicius.georentate.APIClients.UserClients;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.example.zilvinastomkevicius.georentate.R;
import com.example.zilvinastomkevicius.georentate.Update.UpdateGeneralData;

public class LaunchingActivity extends AppCompatActivity {

    private UpdateGeneralData updateGeneralData;
    private UserClients userClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        /*
        if(!isNetworkAvailable()) {
            networkAlert();
        }
        */
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onResume() {
       super.onResume();

       if(isNetworkAvailable()) {
           updateGeneralData = new UpdateGeneralData(this);
           userClients = new UserClients(this);
           updateGeneralData.getAppInfo();
           updateGeneralData.getCheckpointList();

           if(UserObj.USER != null && CheckpointObj.userCheckpointArrayList.size() > 0)
           {
               updateGeneralData.updateUserCheckpoints(CheckpointObj.userCheckpointArrayList);
               userClients.updateUser(UserObj.USER);
           }
       }

       startActivity(new Intent(LaunchingActivity.this, LoginSignUpActivity.class));
       finish();
    }
}
