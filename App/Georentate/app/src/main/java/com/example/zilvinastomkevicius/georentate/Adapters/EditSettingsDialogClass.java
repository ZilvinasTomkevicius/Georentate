package com.example.zilvinastomkevicius.georentate.Adapters;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.APIClients.AppInfoClients;
import com.example.zilvinastomkevicius.georentate.Activites.AdminActivity;
import com.example.zilvinastomkevicius.georentate.Entities.AppInfo;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.AppInfoObj;
import com.example.zilvinastomkevicius.georentate.R;

public class EditSettingsDialogClass extends Dialog implements android.view.View.OnClickListener {

    private AdminActivity mAdminActivity;
    private Button yes, no;

    private EditText mPrivacyPolicyInput;
    private EditText mAboutInput;
    private EditText mConditionsInput;
    private EditText mFbLinkInput;
    private EditText mWebLinkInput;

    private ImageView mCloseButton;

    public EditSettingsDialogClass(AdminActivity adminActivity) {
        super(adminActivity);

        this.mAdminActivity = adminActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_app_settings_layout);
        yes = (Button)findViewById(R.id.apply_edit_button_settings);
        no = (Button)findViewById(R.id.cancel_edit_button_settings);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        mPrivacyPolicyInput = findViewById(R.id.edit_privacy_policy_settings);
        mAboutInput = findViewById(R.id.edit_about_settings);
        mConditionsInput = findViewById(R.id.edit_conditions_settings);
        mCloseButton = findViewById(R.id.close_settings_dialog_ic);
        mFbLinkInput = findViewById(R.id.edit_links_fb_settings);
        mWebLinkInput = findViewById(R.id.edit_links_web_settings);

        mPrivacyPolicyInput.setText(AppInfoObj.APP_INFO.PrivacyPolicy);
        mAboutInput.setText(AppInfoObj.APP_INFO.About);
        mConditionsInput.setText(AppInfoObj.APP_INFO.Conditions);
        mFbLinkInput.setText(AppInfoObj.APP_INFO.FBLink);
        mWebLinkInput.setText(AppInfoObj.APP_INFO.WebLink);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_edit_button_settings:
                addSettings();
                break;
            case R.id.cancel_edit_button_settings:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void addSettings() {
        if(checkInputs()) {
            AppInfo appInfo = new AppInfo();
            appInfo.PrivacyPolicy = mPrivacyPolicyInput.getText().toString();
            appInfo.About = mPrivacyPolicyInput.getText().toString();
            appInfo.Conditions = mConditionsInput.getText().toString();
            appInfo.FBLink = mFbLinkInput.getText().toString();
            appInfo.WebLink = mWebLinkInput.getText().toString();
            appInfo.Version = "1.0";

            AppInfoClients appInfoClients = new AppInfoClients(getContext());
            appInfoClients.updateAppInfo(appInfo);
        }
    }

    public boolean checkInputs() {
        if(mPrivacyPolicyInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Užpildyti privatumo politiką", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mAboutInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Apibūdinti programelę", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mConditionsInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Įvesti naudojimosi sąlygas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
