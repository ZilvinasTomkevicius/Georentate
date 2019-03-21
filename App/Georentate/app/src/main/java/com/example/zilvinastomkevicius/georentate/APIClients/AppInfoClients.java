package com.example.zilvinastomkevicius.georentate.APIClients;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.zilvinastomkevicius.georentate.Activites.LaunchingActivity;
import com.example.zilvinastomkevicius.georentate.Activites.LoginSignUpActivity;
import com.example.zilvinastomkevicius.georentate.Entities.AppInfo;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.AppInfoObj;

/**
 * A class for getting info about the app
 */
public class AppInfoClients {

    private static final int RESPONSE_CODE_OK = 200;
    private static final int RESPONSE_CODE_INTERNAL_SERVER_ERROR = 500;
    private static final int RESPONSE_CODE_BAD_GATEWAY = 502;

    private Context mContext;

    public AppInfoClients(Context context) {
        mContext = context;
    }

    /**
     * Clients
     * @param appInfo
     */

    public void addAppInfo(AppInfo appInfo) {
        toastMessage("Pridedama..");
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.addAppInfo(appInfo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                addAppInfoHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateAppInfo(AppInfo appInfo) {
        toastMessage("Naujinama..");
        RetrofitFactoryInterface retrofitFactoryInterface = RetrofitFactoryClass.Create();
        Call<Void> call = retrofitFactoryInterface.updateAppInfo(appInfo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                updateInfoHandling(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Respones handling
     * @param response
     */

    public void addAppInfoHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            appInfoAlert("App info has been added!");
        } else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            appInfoAlert("Something went wrong.");
        } else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            appInfoAlert("No connection to the server.");
        }
    }

    public void updateInfoHandling(Response<Void> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            appInfoAlert("App info has been updated!");
        } else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            appInfoAlert("Something went wrong.");
        } else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            appInfoAlert("No connection to the server.");
        }
    }

    public void getInfoHandling(Response<AppInfo> response) {
        if(response.code() == RESPONSE_CODE_OK) {
            AppInfoObj.APP_INFO = response.body();
            AppInfoObj.APP_INFO.Version = "1.0";
        } else if(response.code() == RESPONSE_CODE_INTERNAL_SERVER_ERROR) {
            appInfoAlert("Something went wrong.");
        } else if(response.code() == RESPONSE_CODE_BAD_GATEWAY) {
            appInfoAlert("No connection to the server.");
        }

        mContext.startActivity(new Intent(mContext, LoginSignUpActivity.class));
        ((LaunchingActivity)mContext).finish();
    }

    /**
     * Alerts
     * @param message
     */

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
