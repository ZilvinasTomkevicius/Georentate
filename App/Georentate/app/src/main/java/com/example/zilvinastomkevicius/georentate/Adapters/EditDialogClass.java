package com.example.zilvinastomkevicius.georentate.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.example.zilvinastomkevicius.georentate.R;

public class EditDialogClass extends Dialog implements android.view.View.OnClickListener {

    private AdminActivity mAdminActivity;
    private Button edit, cancel;

    private EditText mNameInput;
    private EditText mHintInput;
    private EditText mScanInput;
    private EditText mPointsInput;

    private ImageView mCloseIcon;

    private Checkpoint mCheckpoint;

    public EditDialogClass(AdminActivity adminActivity, Checkpoint checkpoint) {
        super(adminActivity);

        this.mAdminActivity = adminActivity;
        this.mCheckpoint = checkpoint;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_checkpoint_layout);
        edit = (Button) findViewById(R.id.apply_button);
        cancel = (Button) findViewById(R.id.cancel_edit_button);
        edit.setOnClickListener(this);
        cancel.setOnClickListener(this);

        mNameInput = findViewById(R.id.edit_checkpoint_name);
        mHintInput = findViewById(R.id.edit_checkpoint_hint);
        mScanInput = findViewById(R.id.edit_checkpoint_scanString);
        mPointsInput = findViewById(R.id.edit_checkpoint_points);
        mCloseIcon = findViewById(R.id.close_edit_dialog_ic);

        mNameInput.setText(mCheckpoint.Name);
        mHintInput.setText(mCheckpoint.Hint);
        mScanInput.setText(mCheckpoint.Scan);
        mPointsInput.setText(Integer.toString(mCheckpoint.Points));

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
            case R.id.apply_button:
                updateAlert();
                break;
            case R.id.cancel_edit_button:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void updateCheckpoint() {
        if(checkInputs()) {
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.ID = mCheckpoint.ID;
            checkpoint.Name = mNameInput.getText().toString();
            checkpoint.Hint = mHintInput.getText().toString();
            checkpoint.Scan = mScanInput.getText().toString();
            checkpoint.Points = Integer.parseInt(mPointsInput.getText().toString());
            checkpoint.Latitude = mCheckpoint.Latitude;
            checkpoint.Longitude = mCheckpoint.Longitude;

            CheckpointClients checkpointClients = new CheckpointClients(getContext());
            checkpointClients.updateCheckpoint(checkpoint);
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

    public void updateAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateCheckpoint();
            }
        });
        builder.setNegativeButton("No", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
