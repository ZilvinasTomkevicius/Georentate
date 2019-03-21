package com.example.zilvinastomkevicius.georentate.Fragments;


import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.APIClients.UserClients;
import com.example.zilvinastomkevicius.georentate.Activites.LaunchingActivity;
import com.example.zilvinastomkevicius.georentate.Activites.LoginSignUpActivity;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.example.zilvinastomkevicius.georentate.R;

/**
 * A fragment for user profile info displaying and altering
 */
public class ProfileFragment extends Fragment {

    private TextView loginTextView;
    private TextView emailTextView;
    private TextView dateTextView;
    private TextView userPoints;
    private ImageView mExpandLoginImage;
    private FloatingActionButton mLogOffButton;

    private RelativeLayout mLoginRel;

    private TextView mUpdateTextView;
    private EditText mUpdateInput;

    private RelativeLayout mExpandableContainerLogin;

    private static boolean loginExpanded = false;

    /**
     * Overrides
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        loginTextView = view.findViewById(R.id.userLogin_textView);
        emailTextView = view.findViewById(R.id.userEmail_textView);
        dateTextView = view.findViewById(R.id.userStartDate_textView);
        userPoints = view.findViewById(R.id.userPoints_textView);
        mExpandLoginImage = view.findViewById(R.id.expand_more_login);
        mLogOffButton = view.findViewById(R.id.logOff_button);

        mExpandableContainerLogin = view.findViewById(R.id.expandable_container_login);

        mExpandLoginImage.setOnClickListener(new View.OnClickListener() {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View expandView = inflater.inflate(R.layout.expandable_layout_profile, null);

            @Override
            public void onClick(View v) {
                if(!loginExpanded) {
                    onLoginExpand(expandView);
                } else if (loginExpanded) {
                    onLoginCollapse(expandView);
                }
                mUpdateTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateLogin();
                    }
                });
            }
        });
        mLogOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOffAlert();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayUserData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(userPoints != null) {
                userPoints.setText(Float.toString(UserObj.USER.getPoints()));
            }
        }
    }

    public String splitDate() {
        String[] parts = UserObj.USER.getRegisterDate().split("T");
        return parts[0];
    }

    public void displayUserData() {
        loginTextView.setText(UserObj.USER.getLogin());
        emailTextView.setText(UserObj.USER.getEmail());
        dateTextView.setText(splitDate());
        userPoints.setText(Float.toString(UserObj.USER.getPoints()));
    }

    public void updateLogin() {
        if(!mUpdateInput.getText().toString().equals("")) {
            UserObj.USER.setLogin(mUpdateInput.getText().toString());

            updateAlert(mUpdateInput.getText().toString(), "prisijungimą", UserObj.USER);
           // textView.setText(UserObj.USER.Login);
        } else {
            Toast.makeText(getContext(), "Įvesti naują prisijungimą", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Expand view management
     * @param view
     */

    public void onLoginExpand(View view) {
        mUpdateInput = view.findViewById(R.id.update_input);
        mUpdateTextView = view.findViewById(R.id.update_button);

        if(!loginExpanded) {
            mExpandLoginImage.setImageResource(R.drawable.ic_expand_less_yellow_24dp);
            mUpdateInput.setHint("Įvesti naują prisijungimą");

            mExpandableContainerLogin.addView(view);
            loginExpanded = true;
        }
    }

    public void onLoginCollapse(View view) {
        if(loginExpanded) {
            mExpandLoginImage.setImageResource(R.drawable.ic_expand_more_yellow_24dp);

            mExpandableContainerLogin.removeView(view);
            loginExpanded = false;
        }
    }

    public void logOffAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Ar norite atsijungti?");
        builder.setPositiveButton("Taip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), LaunchingActivity.class));
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateAlert(String content, String object, final User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Ar tikrai norite pakeisti savo " + object + " į " + content + "?");
        builder.setPositiveButton("Taip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserClients userClients = new UserClients(getContext());
                userClients.updateUser(user);
            }
        });
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
