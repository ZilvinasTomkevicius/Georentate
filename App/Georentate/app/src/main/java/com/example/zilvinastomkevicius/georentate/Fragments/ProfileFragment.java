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

public class ProfileFragment extends Fragment {

    private TextView loginTextView;
    private TextView emailTextView;
    private TextView dateTextView;
    private TextView userPoints;
    private ImageView mExpandLoginImage;
    private ImageView mExpandEmailImage;
    private FloatingActionButton mLogOffButton;

    private TextView mUpdateTextView;
    private EditText mUpdateInput;

    private RelativeLayout mExpandableContainerLogin;
    private RelativeLayout mExpandableContainerEmail;

    private static boolean loginExpanded = false;
    private static boolean emailExpanded = false;

    //------------------------------- Overrides ---------------------------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        loginTextView = view.findViewById(R.id.userLogin_textView);
        emailTextView = view.findViewById(R.id.userEmail_textView);
        dateTextView = view.findViewById(R.id.userStartDate_textView);
        userPoints = view.findViewById(R.id.userPoints_textView);
        mExpandLoginImage = view.findViewById(R.id.expand_more_login);
        mExpandEmailImage = view.findViewById(R.id.expand_more_email);
        mLogOffButton = view.findViewById(R.id.logOff_button);

        mExpandableContainerLogin = view.findViewById(R.id.expandable_container_login);
        mExpandableContainerEmail = view.findViewById(R.id.expandable_container_email);

        mExpandLoginImage.setOnClickListener(new View.OnClickListener() {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View expandView = inflater.inflate(R.layout.expandable_layout_profile, null);

            @Override
            public void onClick(View v) {
                if(!loginExpanded) {
                    onLoginExpand(expandView);
                }
                else if (loginExpanded) {
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

        mExpandEmailImage.setOnClickListener(new View.OnClickListener() {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View expandView = inflater.inflate(R.layout.expandable_layout_profile, null);

            @Override
            public void onClick(View v) {
                if(!emailExpanded) {
                    onEmailExpand(expandView);
                }
                else if (emailExpanded) {
                    onEmailCollapse(expandView);
                }

                mUpdateTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateEmail();
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
                userPoints.setText(Integer.toString(UserObj.USER.Points));
            }
        }
    }

    public void displayUserData() {
        loginTextView.setText(UserObj.USER.Login);
        emailTextView.setText(UserObj.USER.Email);
        dateTextView.setText(UserObj.USER.RegisterDate.toString());
        userPoints.setText(Integer.toString(UserObj.USER.Points));
    }

    public void setToolbarName() {
        Toolbar toolbar = getActivity().findViewById(R.id.mainToolbar);
        toolbar.setTitle("Profile");
    }

    public void updateLogin() {
        if(!mUpdateInput.getText().toString().equals("")) {

            UserObj.USER.Login = mUpdateInput.getText().toString();

            updateAlert(mUpdateInput.getText().toString(), "Login", UserObj.USER);
            loginTextView.setText(UserObj.USER.Login);
        }
        else {
            Toast.makeText(getContext(), "Enter new username", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateEmail() {
        if(!mUpdateInput.getText().toString().equals("")) {
            UserObj.USER.Email = mUpdateInput.getText().toString();

            updateAlert(mUpdateInput.getText().toString(), "Email", UserObj.USER);
            emailTextView.setText(UserObj.USER.Email);
        }
        else {
            Toast.makeText(getContext(), "Enter new email", Toast.LENGTH_SHORT).show();
        }
    }

    //============================== expand views management =============================

    public void onLoginExpand(View view) {
        mUpdateInput = view.findViewById(R.id.update_input);
        mUpdateTextView = view.findViewById(R.id.update_button);

        if(!loginExpanded) {
            mExpandLoginImage.setImageResource(R.drawable.ic_expand_less_yellow_24dp);
            mUpdateInput.setHint("Enter new username");

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

    public void onEmailExpand(View view) {
        mUpdateInput = view.findViewById(R.id.update_input);
        mUpdateTextView = view.findViewById(R.id.update_button);

        if(!emailExpanded) {
            mExpandEmailImage.setImageResource(R.drawable.ic_expand_less_yellow_24dp);
            mUpdateInput.setHint("Enter new email");

            mExpandableContainerEmail.addView(view);
            emailExpanded = true;
        }
    }

    public void onEmailCollapse(View view) {
        if(emailExpanded) {
            mExpandEmailImage.setImageResource(R.drawable.ic_expand_more_yellow_24dp);

            mExpandableContainerEmail.removeView(view);
            emailExpanded = false;
        }
    }

    public void logOffAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), LaunchingActivity.class));
                getActivity().finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateAlert(String content, String object, final User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to update your " + object + " to " + content + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserClients userClients = new UserClients(getContext());
                userClients.updateUser(user);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
