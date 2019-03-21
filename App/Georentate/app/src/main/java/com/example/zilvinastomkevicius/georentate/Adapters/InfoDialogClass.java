package com.example.zilvinastomkevicius.georentate.Adapters;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zilvinastomkevicius.georentate.APIClients.CheckpointClients;
import com.example.zilvinastomkevicius.georentate.Activites.AdminActivity;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class InfoDialogClass extends Dialog implements android.view.View.OnClickListener {

    private AdminActivity mAdminActivity;
    private Button edit, delete;
    private TextView mCheckpointName;
    private TextView mCheckpointHint;
    private TextView mCheckpointScanString;
    private TextView mCheckpointPoints;

    private Marker mMarker;

    private Checkpoint mCheckpoint;
    private ImageView mCloseIcon;

    private int mID;

    public InfoDialogClass(AdminActivity adminActivity, int ID, Marker marker) {
        super(adminActivity);

        this.mAdminActivity = adminActivity;
        this.mID = ID;
        this.mMarker = marker;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_checkpoint_layout);
        edit = (Button) findViewById(R.id.info_edit_button);
        delete = (Button) findViewById(R.id.info_delete_button);
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);

        mCheckpointName = findViewById(R.id.info_checkpoint_name);
        mCheckpointHint = findViewById(R.id.info_checkpoint_hint);
        mCheckpointScanString = findViewById(R.id.info_checkpoint_scan);
        mCheckpointPoints = findViewById(R.id.info_checkpoint_points);
        mCloseIcon = findViewById(R.id.close_info_dialog_ic);

        mCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCheckpointInfo();
    }

    public void setCheckpointInfo() {
        for(Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
            if(checkpoint.getId() == mID) {
                mCheckpointName.setText(checkpoint.getName());
                mCheckpointHint.setText(checkpoint.getHint());
                mCheckpointScanString.setText(checkpoint.getScan());
                mCheckpointPoints.setText(Float.toString(checkpoint.getPoints()));

                mCheckpoint = new Checkpoint();
                mCheckpoint.setId(mID);
                mCheckpoint.setName(mCheckpointName.getText().toString());
                mCheckpoint.setHint(mCheckpointHint.getText().toString());
                mCheckpoint.setScan(mCheckpointScanString.getText().toString());
                mCheckpoint.setPoints(Integer.parseInt(mCheckpointPoints.getText().toString()));
                LatLng latLng = mMarker.getPosition();
                mCheckpoint.setLatitude(latLng.latitude);
                mCheckpoint.setLongitude(latLng.longitude);

                break;
            }
        }
    }

    public void deleteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Tikrai ištrinti?");
        builder.setPositiveButton("Taip", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMarker.remove();
                deleteCheckpoint();
            }
        });
        builder.setNegativeButton("Ne", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteCheckpoint() {
        CheckpointClients checkpointClients = new CheckpointClients(getContext());
        checkpointClients.deleteCheckpoint(mID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_edit_button:
                EditDialogClass editDialogClass = new EditDialogClass(mAdminActivity, mCheckpoint);
                editDialogClass.show();

                Window window = editDialogClass.getWindow();
                window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

                dismiss();
                break;
            case R.id.info_delete_button:
                deleteAlert();
                break;
            default:
                break;
        }
    }
}
