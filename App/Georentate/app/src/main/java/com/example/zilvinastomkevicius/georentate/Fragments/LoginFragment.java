package com.example.zilvinastomkevicius.georentate.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.zilvinastomkevicius.georentate.Activites.AdminActivity;
import com.example.zilvinastomkevicius.georentate.Activites.MainActivity;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.example.zilvinastomkevicius.georentate.R;

public class LoginFragment extends Fragment {

    private EditText mLoginInput;
    private EditText mPasswordInput;
    private CheckBox mRememberCheck;
    private Button mLoginBtn;
    private ProgressBar mProgressBar;

    //additional variables
    private LoginSignUpClients loginSignUpClients;

    //----------------------------------------- Overrides ---------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginInput = view.findViewById(R.id.loginInput);
        mPasswordInput = view.findViewById(R.id.passwordInput);
        mRememberCheck = view.findViewById(R.id.rememberCheckBox);
        mLoginBtn = view.findViewById(R.id.loginBtn);
        mProgressBar = view.findViewById(R.id.loginProgressBar);

        mProgressBar.setVisibility(View.INVISIBLE);
        mLoginBtn.setText("Login");

        loginSignUpClients = new LoginSignUpClients(getContext());

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInputs()) {
                    logOn();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //======================================= Log on management ==========================

    public boolean checkInputs() {

        if(mLoginInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter login", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(mPasswordInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return true;
        }

        return true;
    }

    public void logOn() {

        if(mLoginInput.getText().toString().equals("admin45") && mPasswordInput.getText().toString().equals("gH12R5")) {
            startActivity(new Intent(getContext(), AdminActivity.class));
            getActivity().finish();
        }
        else {
            User user = new User();
            user.Login = mLoginInput.getText().toString();
            user.Password = mPasswordInput.getText().toString();

            if(mRememberCheck.isChecked()) {
                UserObj.REMEMBER_LOGIN = true;
            }

            mProgressBar.setVisibility(View.VISIBLE);
            mLoginBtn.setText("");

            loginSignUpClients.logOn(user, mProgressBar, mLoginBtn);
        }
    }
}
