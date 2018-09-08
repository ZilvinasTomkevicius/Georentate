package com.example.zilvinastomkevicius.georentate.Fragments;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.zilvinastomkevicius.georentate.APIClients.CheckpointClients;
import com.example.zilvinastomkevicius.georentate.APIClients.ClosestCheckpointFinderTask;
import com.example.zilvinastomkevicius.georentate.APIClients.RoutFinderTask;
import com.example.zilvinastomkevicius.georentate.APIClients.UserClients;
import com.example.zilvinastomkevicius.georentate.Entities.Checkpoint;
import com.example.zilvinastomkevicius.georentate.Entities.UserCheckpoint;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.ActivityObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.CheckpointObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.MapObj;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.UserObj;
import com.example.zilvinastomkevicius.georentate.R;
import com.example.zilvinastomkevicius.georentate.GlobalObjects.RouteObj;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationSource.OnLocationChangedListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 999;

    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final int CAMER_PERMISSION_REQUEST_CODE = 990;

    private static int POSITION;
    private static boolean POSITION_GOT = false;

    //variables
    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;
    private FloatingActionButton mLocationBtn;
    private FloatingActionButton mNavigationButton;
    private FloatingActionButton mCheckpointLocationBtn;
    private FloatingActionButton mDeleteRouteBtn;
    private ProgressBar mRouteSearchBar;
    private TextView mRouteDistance;
    private TextView mRouteDuration;
    private TextView mCheckpointName;
    private TextView mCheckpointPoints;
    private TextView mNearestCheckpoint;

    private ImageView mExpandImageView;
    private RelativeLayout mExpandableContainer;
    private RelativeLayout mMainInfoLayout;
    private TextView mScanTextView;
    private TextView mExpandableTextViewCompleted;
    private static boolean expanded = false;
    private View expandView;

    private TextView mHintTextView;

    private FrameLayout mScanFrameLayout;
    ZXingScannerView scannerView;

    private View view;

    //additional variables
    ArrayList<Polyline> polList;
    private static Location currentLocation;
    private static Checkpoint CHECKPOINT_FOR_ROUTE;

    private CheckpointClients checkpointClients;
    private UserClients userClients;
    //------------------------------------ Overrides -----------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);

        mLocationBtn = view.findViewById(R.id.myLocationBtn);
        mNavigationButton = view.findViewById(R.id.navigation_button);
        mRouteSearchBar = view.findViewById(R.id.routeSearchProgressBar);
        mRouteDistance = view.findViewById(R.id.checkpoint_distance);
        mRouteDuration = view.findViewById(R.id.checkpoint_time);
        mCheckpointName = view.findViewById(R.id.checkpoint_name);
        mCheckpointPoints = view.findViewById(R.id.checkpoint_points);
        mCheckpointLocationBtn = view.findViewById(R.id.checkpointLocationBtn);
        mDeleteRouteBtn = view.findViewById(R.id.deleteRouteBtn);
        mNearestCheckpoint = view.findViewById(R.id.nearestCheck_static);
        mExpandImageView = view.findViewById(R.id.expand_imageView);
        mExpandableContainer = view.findViewById(R.id.expandable_container);
        mMainInfoLayout = view.findViewById(R.id.mainInfo_layout);
        mScanFrameLayout = view.findViewById(R.id.scanView_container);

        mRouteSearchBar.setVisibility(View.INVISIBLE);
        mDeleteRouteBtn.setVisibility(View.INVISIBLE);

        mNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CHECKPOINT_FOR_ROUTE != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Set new route?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            MapObj.DESTINATION_CHECKPOINT = new Checkpoint();
                            MapObj.DESTINATION_CHECKPOINT = CHECKPOINT_FOR_ROUTE;

                            getRouteInfo(CHECKPOINT_FOR_ROUTE.Latitude, CHECKPOINT_FOR_ROUTE.Longitude);
                            mDeleteRouteBtn.setVisibility(View.VISIBLE);
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
        });

        mCheckpointLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MapObj.CHECKPOINT_LOCATE.equals("Nearest checkpoint")) {
                    if(POSITION_GOT) {
                        mNearestCheckpoint.setText("Nearest checkpooint");
                        setClosestCheckpointInfo();
                        moveCamera(new LatLng(CHECKPOINT_FOR_ROUTE.Latitude, CHECKPOINT_FOR_ROUTE.Longitude), MapObj.DEFAULT_ZOOM);
                    }
                }
                else if(MapObj.CHECKPOINT_LOCATE.equals("Destination")) {
                    if(MapObj.DESTINATION_CHECKPOINT != null) {
                        moveCamera(new LatLng(MapObj.DESTINATION_CHECKPOINT.Latitude, MapObj.DESTINATION_CHECKPOINT.Longitude), MapObj.DEFAULT_ZOOM);
                    }
                }
                else if(MapObj.CHECKPOINT_LOCATE.equals("Checked")) {
                    if(CHECKPOINT_FOR_ROUTE != null) {
                        moveCamera(new LatLng(CHECKPOINT_FOR_ROUTE.Latitude, CHECKPOINT_FOR_ROUTE.Longitude), MapObj.DEFAULT_ZOOM);
                    }
                }
            }
        });

        mDeleteRouteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(polList != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Delete route?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteRoute();
                            mDeleteRouteBtn.setVisibility(View.INVISIBLE);
                            MapObj.DESTINATION_CHECKPOINT = null;
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
        });

        mExpandImageView.setOnClickListener(new View.OnClickListener() {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View expandView = layoutInflater.inflate(R.layout.expandable_layout_map, null);

            @Override
            public void onClick(View v) {
                if(!expanded) {
                    onExpand(expandView);
                    expanded = true;
                }

                else if(expanded) {
                    onCollapse(expandView);
                    expanded = false;
                }

                mScanTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(CHECKPOINT_FOR_ROUTE != null) {
                            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View scanView = layoutInflater.inflate(R.layout.scan_layout, null);

                            startCamera(scanView);
                        }
                    }
                });
            }
        });

        mExpandableContainer.setLayoutTransition(new LayoutTransition());
        mScanFrameLayout.setLayoutTransition(new LayoutTransition());

        return view;
    }

    public boolean checkAPILevel() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(checkAPILevel()) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
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

    public void locationAlert() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setMessage("You will need to turn your location on in order to use the app");
        builder.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            locationAlert();
        }
        else {
            getLocationPermission();
        }

        if(getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && ActivityObj.IS_SCAN_CAMERA) {
                    stopCamera();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        setCheckpointsOnMap();

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if(currentLocation != null) {
                getCheckpointDistances(currentLocation);
            }

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);

            mLocationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getDeviceLocation();

                    if(currentLocation != null) {
                        getCheckpointDistances(currentLocation);
                    }
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
       //     setToolbarName();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(CheckpointObj.CLOSEST_CHECKPOINTS.size() > 0) {
            for(int i = 0; i < CheckpointObj.checkpointArrayList.size(); i++) {
                Checkpoint checkpoint = CheckpointObj.checkpointArrayList.get(i);

                if(checkpoint.Name.equals(marker.getTitle())) {
                    mCheckpointName.setText(checkpoint.Name);
                    mCheckpointPoints.setText(Integer.toString(checkpoint.Points) + " points");

                    if(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ExactDistance < 1000) {
                        mRouteDistance.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ExactDistance + " m");
                    }
                    else {
                        mRouteDistance.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ApproximateDistance);
                    }

                    mRouteDuration.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).TravelTime);

                    mNearestCheckpoint.setText("Checkpoint");

                    CHECKPOINT_FOR_ROUTE = new Checkpoint();
                    CHECKPOINT_FOR_ROUTE = checkpoint;
                    break;
                }
            }
        }

        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

        moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), MapObj.DEFAULT_ZOOM);
    }
    //========================================= Closest checkpoint management =============================

    //putting existing checkpoints
    public void setCheckpointsOnMap() {

        for (Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(checkpoint.Name)
                    .position(new LatLng(checkpoint.Latitude, checkpoint.Longitude));

            for(UserCheckpoint userCheckpoint : CheckpointObj.userCheckpointArrayList) {
                if(userCheckpoint.CheckpointID == checkpoint.ID) {
                    if(userCheckpoint.Completed) {
                        markerOptions.icon(getMarkerIconColor("#00796b"));
                    }
                    else {
                        markerOptions.icon(getMarkerIconColor("#ffb300"));
                    }

                    break;
                }
            }

            mMap.addMarker(markerOptions);
        }
    }

    //custom checkpoint color
    public BitmapDescriptor getMarkerIconColor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    //getting all checkpoints distances from the current location
    public void getCheckpointDistances(Location currentLocation) {

        mRouteSearchBar.setVisibility(View.VISIBLE);

        ClosestCheckpointFinderTask closestCheckpointFinderTask = new ClosestCheckpointFinderTask(MapFragment.this);
        String[] uriArray = new String[CheckpointObj.checkpointArrayList.size()];

        for(int i = 0; i < CheckpointObj.checkpointArrayList.size(); i++) {
            Checkpoint checkpoint = CheckpointObj.checkpointArrayList.get(i);

            String uri = RouteObj.BASE_URI +
                    currentLocation.getLatitude() + "," + currentLocation.getLongitude() +
                    RouteObj.DESTINATION_URI +
                    checkpoint.Latitude + "," + checkpoint.Longitude +
                    RouteObj.KEY_URI;

            uriArray[i] = uri;
        }

        closestCheckpointFinderTask.execute(uriArray);
    }

    //finding which of all checkpoints is the closest one
    public int getClosestCheckpoint() {

        int closestPosition = 0;
        int closestDistance = CheckpointObj.CLOSEST_CHECKPOINTS.get(0).ExactDistance;

        for(int i = 1; i < CheckpointObj.CLOSEST_CHECKPOINTS.size(); i++) {

            if(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ExactDistance < closestDistance) {
                closestDistance = CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ExactDistance;
                closestPosition = i;
            }
        }

        return closestPosition;
    }

    //setting info on the bar about checked checkpoint
    public void setClosestCheckpointInfo() {

        POSITION = getClosestCheckpoint();

        Checkpoint checkpoint = CheckpointObj.checkpointArrayList.get(POSITION);
        CHECKPOINT_FOR_ROUTE = new Checkpoint();
        CHECKPOINT_FOR_ROUTE = checkpoint;

        mCheckpointName.setText(checkpoint.Name);
        mCheckpointPoints.setText(Integer.toString(checkpoint.Points) + " points");

        if(CheckpointObj.CLOSEST_CHECKPOINTS.get(POSITION).ExactDistance < 1000) {
            mRouteDistance.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(POSITION).ExactDistance + " m");
        }
        else {
            mRouteDistance.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(POSITION).ApproximateDistance);
        }

        mRouteDuration.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(POSITION).TravelTime);
        POSITION_GOT = true;

        mRouteSearchBar.setVisibility(View.INVISIBLE);
    }

    //=========================================== Route management =====================================

    //collecting info about destination and executing Route finding AsyncTask
    public void getRouteInfo(double lat, double lng) {

        mRouteSearchBar.setVisibility(View.VISIBLE);

        String uri = RouteObj.BASE_URI +
                currentLocation.getLatitude() + "," + currentLocation.getLongitude() +
                RouteObj.DESTINATION_URI +
                lat + "," + lng +
                RouteObj.KEY_URI;

        RoutFinderTask routFinderTask = new RoutFinderTask(MapFragment.this);
        routFinderTask.execute(uri);
    }

    //drawing gotten route
    public void drawRoute(ArrayList<String> polylineArrayList) {

        deleteRoute();
        polList = new ArrayList<>();

        for(String point : polylineArrayList) {

            PolylineOptions polylineOptions = new PolylineOptions()
                    .color(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .width(15)
                    .addAll(PolyUtil.decode(point));

            polList.add(mMap.addPolyline(polylineOptions));
        }

        mRouteSearchBar.setVisibility(View.INVISIBLE);
    }

    public void deleteRoute() {

        if(polList != null) {
            for(Polyline polyline : polList) {
                polyline.remove();
            }
            polList.clear();
        }
    }

    //======================================= Map initialization and permission management ===================

    private void initializeMap() {

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            if(mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if(task.isSuccessful()) {
                            currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), MapObj.DEFAULT_ZOOM);

                            getCheckpointDistances(currentLocation);
                        }

                        else {
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_LONG).show();
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

        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initializeMap();
            }

            else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
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

    //=============================== View moving =========================================

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    //================================== Expandable view management =======================

    public void onExpand(View expandView) {
        mCheckpointLocationBtn.setVisibility(View.INVISIBLE);
        mDeleteRouteBtn.setVisibility(View.INVISIBLE);

        mHintTextView = expandView.findViewById(R.id.checkpoint_hint);
        mScanTextView = expandView.findViewById(R.id.scan_textView_click);
        mExpandImageView.setImageResource(R.drawable.ic_expand_less_black_24dp);
        mExpandableTextViewCompleted = expandView.findViewById(R.id.completed_expandable_textView);
        mExpandableTextViewCompleted.setVisibility(View.INVISIBLE);
        mScanTextView.setVisibility(View.INVISIBLE);

        if(CHECKPOINT_FOR_ROUTE != null) {
            mHintTextView.setText(CHECKPOINT_FOR_ROUTE.Hint);

            for(UserCheckpoint userCheckpoint : CheckpointObj.userCheckpointArrayList) {
                if(userCheckpoint.CheckpointID == CHECKPOINT_FOR_ROUTE.ID) {
                    if(userCheckpoint.Completed) {
                        mScanTextView.setVisibility(View.INVISIBLE);
                        mExpandableTextViewCompleted.setVisibility(View.VISIBLE);
                        break;
                    }
                    else {
                        mScanTextView.setVisibility(View.VISIBLE);
                        mExpandableTextViewCompleted.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

        else {
            mHintTextView.setText("Loading..");
        }

        mExpandableContainer.addView(expandView);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
         //   mExpandableContainer.setElevation(6);
         //   mMainInfoLayout.setElevation(0);
        }

    }

    public void onCollapse(View expandView) {
   //     mMainInfoLayout.setElevation(6);
     //   mExpandableContainer.setElevation(0);
        mExpandableContainer.removeView(expandView);

        mExpandImageView.setImageResource(R.drawable.ic_expand_more_black_24dp);

        mCheckpointLocationBtn.setVisibility(View.VISIBLE);
        mDeleteRouteBtn.setVisibility(View.VISIBLE);
    }

    //========================================== Scan view management =============================

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {

        @Override
        public void handleResult(Result result) {
            stopCamera();
            onPostScanned(result);
        }
    }

    public void startCamera(View scanView) {
        scannerView = new ZXingScannerView(getContext());

        mScanFrameLayout.addView(scannerView);

        scannerView.setResultHandler(new MapFragment.ZXingScannerResultHandler());
        cameraPermission();
    }

    public void stopCamera() {
        scannerView.stopCamera();
        mScanFrameLayout.removeView(scannerView);
        ActivityObj.IS_SCAN_CAMERA = false;
    }

    public void cameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(getContext(), CAMERA_PERMISSION ) == PackageManager.PERMISSION_GRANTED) {
            scannerView.startCamera();
            ActivityObj.IS_SCAN_CAMERA = true;
        }

        else {
            ActivityCompat.requestPermissions(getActivity(), permissions, CAMER_PERMISSION_REQUEST_CODE);
            if(ContextCompat.checkSelfPermission(getContext(), CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                scannerView.startCamera();
                ActivityObj.IS_SCAN_CAMERA = true;
            }
        }
    }

    public void onPostScanned(Result result) {
        String scanResult = result.getText();

        StringBuilder stringBuilder = new StringBuilder();

        if(scanResult.equals(CHECKPOINT_FOR_ROUTE.Scan)) {
            onPostScanAlert("Checkpoint completed!");

            for(UserCheckpoint userCheckpoint : CheckpointObj.userCheckpointArrayList) {
                if(CHECKPOINT_FOR_ROUTE.ID == userCheckpoint.CheckpointID) {
                    userCheckpoint.Completed = true;
                    UserObj.USER.Points += CHECKPOINT_FOR_ROUTE.Points;

                    checkpointClients = new CheckpointClients(getContext());
                    userClients = new UserClients(getContext());

                    checkpointClients.updateUserCheckpoint(userCheckpoint);
                    userClients.updateUser(UserObj.USER);
                    CHECKPOINT_FOR_ROUTE = null;
                    break;
                }
            }
            setCheckpointsOnMap();
        }
        else {
            onPostScanAlert("Wrong checkpoint.");
        }
    }

    public void onPostScanAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //=================================== Toolbar ====================================

    public void setToolbarName() {
        Toolbar toolbar = getActivity().findViewById(R.id.mainToolbar);
        toolbar.setTitle("Map");
    }
}
