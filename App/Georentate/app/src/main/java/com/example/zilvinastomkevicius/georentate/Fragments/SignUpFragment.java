package com.example.zilvinastomkevicius.georentate.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.APIClients.LoginSignUpClients;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.R;

/**
 * A fragment for signup management
 */
public class SignUpFragment extends Fragment{

    private EditText mLoginInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mPasswordRepInput;
    private Button mSignUpBtn;
    private ProgressBar mProgressBar;

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

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mLoginInput = view.findViewById(R.id.createLoginInput);
        mEmailInput = view.findViewById(R.id.createEmailInput);
        mPasswordInput = view.findViewById(R.id.createPasswordInput);
        mPasswordRepInput = view.findViewById(R.id.createPasswordInputRep);
        mSignUpBtn = view.findViewById(R.id.signUpBtn);
        mProgressBar = view.findViewById(R.id.signUpProgressBar);

        mProgressBar.setVisibility(View.INVISIBLE);
        mSignUpBtn.setText("Sign up");

        loginSignUpClients = new LoginSignUpClients(getContext());

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if(checkIputs()) {
                        signUp();
                    }
                } else {
                    networkAlert();
                }
            }
        });
        return view;
    }

    /**
     * Signup methods
     * @return
     */

    public boolean checkIputs() {
        if(mLoginInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Create login", Toast.LENGTH_SHORT).show();
            return false;
        } else if(mEmailInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        } else if(mPasswordInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Create password", Toast.LENGTH_SHORT).show();
            return false;
        } else if(mPasswordRepInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Confirm password", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!mPasswordInput.getText().toString().equals(mPasswordRepInput.getText().toString())) {
            Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void signUp() {
        User user = new User();
        user.setLogin(mLoginInput.getText().toString());
        user.setEmail(mEmailInput.getText().toString());
        user.setPassword(mPasswordInput.getText().toString());

        mProgressBar.setVisibility(View.VISIBLE);
        mSignUpBtn.setText("");

        loginSignUpClients.addUser(user, mProgressBar, mSignUpBtn);
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
