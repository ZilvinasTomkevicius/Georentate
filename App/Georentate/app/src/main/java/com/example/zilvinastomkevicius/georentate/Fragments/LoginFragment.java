package com.example.zilvinastomkevicius.georentate.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.APIClients.LoginSignUpClients;
import com.example.zilvinastomkevicius.georentate.Activites.AdminActivity;
import com.example.zilvinastomkevicius.georentate.Activites.MainActivity;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.example.zilvinastomkevicius.georentate.R;

/**
 * A fragment for Login UI and functionality
 */
public class LoginFragment extends Fragment {

    private EditText mLoginInput;
    private EditText mPasswordInput;
    private CheckBox mRememberCheck;
    private Button mLoginBtn;
    private ProgressBar mProgressBar;
    private ImageView mViewPassword;

    public static boolean passwordVisable = false;

    //additional variables
    private LoginSignUpClients loginSignUpClients;

    /**
     * Overrides
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginInput = view.findViewById(R.id.loginInput);
        mPasswordInput = view.findViewById(R.id.passwordInput);
        mRememberCheck = view.findViewById(R.id.rememberCheckBox);
        mLoginBtn = view.findViewById(R.id.loginBtn);
        mProgressBar = view.findViewById(R.id.loginProgressBar);
        mViewPassword = view.findViewById(R.id.view_password);

        mProgressBar.setVisibility(View.INVISIBLE);
        mLoginBtn.setText("Prisijungti");

        loginSignUpClients = new LoginSignUpClients(getContext());

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if(checkInputs()) {
                        logOn();
                    }
                } else {
                    networkAlert();
                }
            }
        });

        mViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordVisable) {
                    mPasswordInput.setTransformationMethod(null);
                    mViewPassword.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    passwordVisable = true;
                } else {
                    mPasswordInput.setTransformationMethod(new PasswordTransformationMethod());
                    mViewPassword.setImageResource(R.drawable.ic_visibility_black_24dp);
                    passwordVisable = false;
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Login management methods
     * @return
     */

    public boolean checkInputs() {
        if(mLoginInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Prisijungimas", Toast.LENGTH_SHORT).show();
            return false;
        } else if(mPasswordInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Slaptažodis", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

    public void logOn() {
        if(mLoginInput.getText().toString().equals("admin45") && mPasswordInput.getText().toString().equals("gH12R5")) {
            startActivity(new Intent(getContext(), AdminActivity.class));
            getActivity().finish();
        } else {
            User user = new User();
            user.setLogin(mLoginInput.getText().toString());
            user.setPassword(mPasswordInput.getText().toString());

            if(mRememberCheck.isChecked()) {
                UserObj.REMEMBER_LOGIN = true;
            }

            mProgressBar.setVisibility(View.VISIBLE);
            mLoginBtn.setText("");

            loginSignUpClients.logOn(user, mProgressBar, mLoginBtn);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void networkAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Turn Wi-Fi ir Mobile Data on");
        builder.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent viewIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(viewIntent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
