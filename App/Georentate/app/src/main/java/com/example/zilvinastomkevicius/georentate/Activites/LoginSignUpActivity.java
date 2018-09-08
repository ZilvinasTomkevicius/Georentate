package com.example.zilvinastomkevicius.georentate.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
                UserObj.USER.ID = sharedPreferences.getInt("ID", 0);
                UserObj.USER.Login = login;
                UserObj.USER.Email = sharedPreferences.getString("EMAIL", null);
                UserObj.USER.Password = sharedPreferences.getString("PASSWORD", null);
                UserObj.USER.RegisterDate = sharedPreferences.getString("REGISTERDATE", null);
                UserObj.USER.Points = sharedPreferences.getInt("POINTS", 0);

                String jsonCheckpoints = sharedPreferences.getString("CHECKPOINTS", null);
                serializeFromJson(jsonCheckpoints);

                UserObj.IS_LOGGED = true;

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
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

        if(UserObj.REMEMBER_LOGIN && UserObj.USER != null) {

            SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("ID", UserObj.USER.ID);
            editor.putString("LOGIN", UserObj.USER.Login);
            editor.putString("EMAIL", UserObj.USER.Email);
            editor.putString("PASSWORD", UserObj.USER.Password);
            editor.putString("REGISTERDATE", UserObj.USER.RegisterDate);
            editor.putInt("POINTS", UserObj.USER.Points);
            editor.putString("CHECKPOINTS", serializeToJson());
            editor.apply();
        }
    }

    public String serializeToJson() {
        Gson gson = new Gson();
        String jsonCheckpoints = gson.toJson(CheckpointObj.userCheckpointArrayList);

        return jsonCheckpoints;
    }

    public void serializeFromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserCheckpoint>>(){}.getType();

        CheckpointObj.userCheckpointArrayList = gson.fromJson(json, type);
    }
}
