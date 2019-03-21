package com.example.zilvinastomkevicius.georentate.Activites;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.zilvinastomkevicius.georentate.APIClients.CheckpointClients;
import com.example.zilvinastomkevicius.georentate.Adapters.AddDialogClass;
import com.example.zilvinastomkevicius.georentate.Adapters.AddSettingsDialogClass;
import com.example.zilvinastomkevicius.georentate.Adapters.EditSettingsDialogClass;
import com.example.zilvinastomkevicius.georentate.Adapters.InfoDialogClass;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.User;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.AppInfoObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.MapObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.example.zilvinastomkevicius.georentate.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 999;

    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private FloatingActionButton mLogOffFloatingActionButton;
    private FloatingActionButton mAddSettingsActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        CheckpointObj.userCheckpointArrayList.clear();

        mLogOffFloatingActionButton = findViewById(R.id.admin_logoff);
        mAddSettingsActionButton = findViewById(R.id.admin_add_settings);
        mLogOffFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOffAlert();
            }
        });

        mAddSettingsActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppInfoObj.APP_INFO != null) {
                    if(AppInfoObj.APP_INFO.PrivacyPolicy != null) {
                        EditSettingsDialogClass editSettingsDialogClass = new EditSettingsDialogClass(AdminActivity.this);
                        editSettingsDialogClass.show();

                        Window window = editSettingsDialogClass.getWindow();
                        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    }
                }
                else {
                    AddSettingsDialogClass addSettingsDialogClass = new AddSettingsDialogClass(AdminActivity.this);
                    addSettingsDialogClass.show();

                    Window window = addSettingsDialogClass.getWindow();
                    window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                }
            }
        });

     //  checkLocationCondition();
    }

    public void checkLocationCondition() {
        if(checkAPILevel()) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                locationAlert();
            }
            else {
                getLocationPermission();
            }
        }
        else {
            getLocationPermission();
        }
    }

    public boolean checkAPILevel() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        checkLocationCondition();
    }

    public void locationAlert() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("You will need to turn your location on in order to use the app");
        builder.setPositiveButton("Įjungti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void logOffAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Išeiti?");
        builder.setPositiveButton("Taip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserObj.LOG_OFF = true;

                if(CheckpointObj.NEW_CHECKPOINTS) {
                    CheckpointClients checkpointClients = new CheckpointClients(AdminActivity.this);
                    checkpointClients.getCheckpointList();
                }
                else {
                    startActivity(new Intent(AdminActivity.this, LaunchingActivity.class));
                    finish();
                }
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

    public void exitAppAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Išeiti?");
        builder.setPositiveButton("Taip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserObj.EXIT_APP = true;

                if(CheckpointObj.NEW_CHECKPOINTS) {
                    CheckpointClients checkpointClients = new CheckpointClients(AdminActivity.this);
                    checkpointClients.getCheckpointList();
                }
                else {
                    startActivity(new Intent(AdminActivity.this, LaunchingActivity.class));
                    finish();
                }
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

    @Override
    public void onBackPressed() {
        exitAppAlert();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        if (mLocationPermissionGranted) {
           // getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

          //  mMap.setMyLocationEnabled(true);
            setUpCheckpoints();
        }
    }

    public void setUpCheckpoints() {
        for(Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(checkpoint.getLatitude(), checkpoint.getLongitude()))
                    .snippet(checkpoint.getName())
                    .title(checkpoint.getName());

            mMap.addMarker(markerOptions);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int id = 0;
        for(Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
            if(checkpoint.getName().equals(marker.getTitle())) {
                id = checkpoint.getId();
                break;
            }
        }

        InfoDialogClass infoDialogClass = new InfoDialogClass(AdminActivity.this, id, marker);
        infoDialogClass.show();

        Window window = infoDialogClass.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        AddDialogClass addDialogClass = new AddDialogClass(AdminActivity.this, latLng);
        addDialogClass.show();

        Window window = addDialogClass.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng);

        mMap.addMarker(markerOptions);
    }

    private void initializeMap() {

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.admin_map);
        supportMapFragment.getMapAsync(this);
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if(mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if(task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), MapObj.DEFAULT_ZOOM);
                        }

                        else {

                        }
                    }
                });
            }
        }
        catch (SecurityException e) {

        }
    }

    private void getLocationPermission() {

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this, COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initializeMap();
            }

            else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;

        switch (requestCode) {

            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0) {
                    for(int i = 0; i < grantResults.length; i++) {

                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }

                    mLocationPermissionGranted = true;
                    initializeMap();
                }
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
}
