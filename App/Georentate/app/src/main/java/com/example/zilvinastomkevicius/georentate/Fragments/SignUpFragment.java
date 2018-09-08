package com.example.zilvinastomkevicius.georentate.Fragments;

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
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.R;

public class SignUpFragment extends Fragment{

    private EditText mLoginInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mPasswordRepInput;
    private CheckBox mLoginAfterCheck;
    private Button mSignUpBtn;
    private ProgressBar mProgressBar;

    //additional variables
    private LoginSignUpClients loginSignUpClients;

    //------------------------------------------ Overrides ---------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mLoginInput = view.findViewById(R.id.createLoginInput);
        mEmailInput = view.findViewById(R.id.createEmailInput);
        mPasswordInput = view.findViewById(R.id.createPasswordInput);
        mPasswordRepInput = view.findViewById(R.id.createPasswordInputRep);
        mLoginAfterCheck = view.findViewById(R.id.loginAfterCheckBox);
        mSignUpBtn = view.findViewById(R.id.signUpBtn);
        mProgressBar = view.findViewById(R.id.signUpProgressBar);

        mProgressBar.setVisibility(View.INVISIBLE);
        mSignUpBtn.setText("Sign up");

        loginSignUpClients = new LoginSignUpClients(getContext());

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkIputs()) {
                    signUp();
                }
            }
        });

        return view;
    }

    //======================================== sign up on management =========================

    public boolean checkIputs() {

        if(mLoginInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Create login", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(mEmailInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(mPasswordInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Create password", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(mPasswordRepInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(!mPasswordInput.getText().toString().equals(mPasswordRepInput.getText().toString())) {
            Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void signUp() {
        User user = new User();
        user.Login = mLoginInput.getText().toString();
        user.Email = mEmailInput.getText().toString();
        user.Password = mPasswordInput.getText().toString();

        boolean loginAfter = false;

        if(mLoginAfterCheck.isChecked()) {
            loginAfter = true;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mSignUpBtn.setText("");

        loginSignUpClients.addUser(user, loginAfter, mProgressBar, mSignUpBtn);
    }
}
