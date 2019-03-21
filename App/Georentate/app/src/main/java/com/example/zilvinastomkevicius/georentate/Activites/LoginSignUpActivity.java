package com.example.zilvinastomkevicius.georentate.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zilvinastomkevicius.georentate.APIClients.AppInfoClients;
import com.example.zilvinastomkevicius.georentate.APIClients.CheckpointClients;
import com.example.zilvinastomkevicius.georentate.Entities.AppInfo;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.example.zilvinastomkevicius.georentate.Fragments.LoginFragment;
import com.example.zilvinastomkevicius.georentate.Fragments.MapFragment;
import com.example.zilvinastomkevicius.georentate.Fragments.MarkerListFragment;
import com.example.zilvinastomkevicius.georentate.Fragments.MessagesFragment;
import com.example.zilvinastomkevicius.georentate.Fragments.ProfileFragment;
import com.example.zilvinastomkevicius.georentate.Fragments.SettingsFragment;
import com.example.zilvinastomkevicius.georentate.Fragments.SignUpFragment;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.AppInfoObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.example.zilvinastomkevicius.georentate.R;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LoginSignUpActivity extends AppCompatActivity {

    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    private CheckpointClients checkpointClients;
    private AppInfoClients appInfoClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        viewPager = findViewById(R.id.viewContainer);
        tabLayout = findViewById(R.id.loginSignUpTabLayout);

        sectionsPagerAdapter = new LoginSignUpActivity.SectionsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        if(UserObj.IS_LOGGED) {
            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            UserObj.IS_LOGGED = false;
        }
        else {
            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            String login = sharedPreferences.getString("LOGIN", null);

            if(login != null) {
                UserObj.USER = new User();
                UserObj.USER.setId(sharedPreferences.getInt("ID", 0));
                UserObj.USER.setLogin(login);
                UserObj.USER.setEmail(sharedPreferences.getString("EMAIL", null));
                UserObj.USER.setPassword(sharedPreferences.getString("PASSWORD", null));
                UserObj.USER.setRegisterDate(sharedPreferences.getString("REGISTERDATE", null));
                UserObj.USER.setPoints(sharedPreferences.getFloat("POINTS", 0));

                String jsonUserCheckpoints = sharedPreferences.getString("USER_CHECKPOINTS", null);
                serializeFromJsonUserCheckpoints(jsonUserCheckpoints);

                String jsonAllCheckpoints = sharedPreferences.getString("CHECKPOINTS", null);
                serializeFromJsonAllCheckpoints(jsonAllCheckpoints);

                String jsonAppInfo = sharedPreferences.getString("APP_INFO", null);
                serializeFromJsonAppInfo(jsonAppInfo);

                UserObj.IS_LOGGED = true;

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkLocationCondition();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LoginFragment();
                case 1:
                    return new SignUpFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("CHECKPOINTS", serializeToJsonAllCheckpoints());

        if(AppInfoObj.APP_INFO != null) {
            editor.putString("APP_INFO", serializeToJsonAppInfo());
        }

        editor.apply();

        if(UserObj.REMEMBER_LOGIN && UserObj.USER != null) {
            editor.putInt("ID", UserObj.USER.getId());
            editor.putString("LOGIN", UserObj.USER.getLogin());
            editor.putString("EMAIL", UserObj.USER.getEmail());
            editor.putString("PASSWORD", UserObj.USER.getPassword());
            editor.putString("REGISTERDATE", UserObj.USER.getRegisterDate());
            editor.putFloat("POINTS", UserObj.USER.getPoints());
            editor.putString("USER_CHECKPOINTS", serializeToJsonUserCheckpoints());
            editor.apply();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //===================================== ArrayList serialization to json ===========================
    public String serializeToJsonUserCheckpoints() {
        Gson gson = new Gson();
        String jsonCheckpoints = gson.toJson(CheckpointObj.userCheckpointArrayList);

        return jsonCheckpoints;
    }

    public String serializeToJsonAllCheckpoints() {
        Gson gson = new Gson();
        String jsonCheckpoints = gson.toJson(CheckpointObj.checkpointArrayList);

        return jsonCheckpoints;
    }

    public String serializeToJsonAppInfo() {
        Gson gson = new Gson();
        String jsonAppInfo = gson.toJson(AppInfoObj.APP_INFO);

        return  jsonAppInfo;
    }

    //====================================== ArrayList serialization from json ========================

    public void serializeFromJsonUserCheckpoints(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserCheckpoint>>(){}.getType();

        CheckpointObj.userCheckpointArrayList = gson.fromJson(json, type);
    }

    public void serializeFromJsonAllCheckpoints(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Checkpoint>>(){}.getType();

        CheckpointObj.checkpointArrayList = gson.fromJson(json, type);
    }

    public void serializeFromJsonAppInfo(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<AppInfo>(){}.getType();

        AppInfoObj.APP_INFO = gson.fromJson(json, type);
    }

    //====================================== Location ==============================================

    public boolean checkAPILevel() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return true;
        }
        return false;
    }

    public void checkLocationCondition() {
        if(checkAPILevel()) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                locationAlert();
            }
        }
    }

    public void locationAlert() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("You will need to turn your location on in order to use the app");
        builder.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
