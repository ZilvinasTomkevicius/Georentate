package com.example.zilvinastomkevicius.georentate.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zilvinastomkevicius.georentate.APIClients.CheckpointClients;
import com.example.zilvinastomkevicius.georentate.Activites.AdminActivity;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class AddDialogClass extends Dialog implements android.view.View.OnClickListener {

    private AdminActivity mAdminActivity;
    private Button yes, no;
    private LatLng mLatLng;

    private EditText mNameInput;
    private EditText mHintInput;
    private EditText mScanInput;
    private EditText mPointsInput;

    private ImageView mCloseIcon;

    public AddDialogClass(AdminActivity adminActivity, LatLng latLng) {
        super(adminActivity);

        this.mAdminActivity = adminActivity;
        this.mLatLng = latLng;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_checkpoint_layout);
        yes = (Button) findViewById(R.id.yes_button);
        no = (Button) findViewById(R.id.cancel_button);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        mNameInput = findViewById(R.id.add_checkpoint_name);
        mHintInput = findViewById(R.id.add_checkpoint_hint);
        mScanInput = findViewById(R.id.add_checkpoint_scanString);
        mPointsInput = findViewById(R.id.add_checkpoint_points);
        mCloseIcon = findViewById(R.id.close_add_dialog_ic);

        mCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_button:
                addCheckpoint();
                break;
            case R.id.cancel_button:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void addCheckpoint() {
        if(checkInputs()) {
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.Name = mNameInput.getText().toString();
            checkpoint.Hint = mHintInput.getText().toString();
            checkpoint.Scan = mScanInput.getText().toString();
            checkpoint.Points = Integer.parseInt(mPointsInput.getText().toString());
            checkpoint.Latitude = mLatLng.latitude;
            checkpoint.Longitude = mLatLng.longitude;

            CheckpointClients checkpointClients = new CheckpointClients(getContext());
            checkpointClients.addCheckpoint(checkpoint);

            CheckpointObj.NEW_CHECKPOINTS = true;
            CheckpointObj.newCheckpointList.add(checkpoint.Name);
        }
    }

    public boolean checkInputs() {
        if(mNameInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mHintInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter hint", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mScanInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter scan string", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mPointsInput.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter points", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
