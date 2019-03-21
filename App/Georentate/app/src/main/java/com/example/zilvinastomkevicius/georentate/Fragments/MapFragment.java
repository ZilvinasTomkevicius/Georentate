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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.LinearLayout;
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
import com.example.zilvinastomkevicius.georentate.Update.UpdateGeneralData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnMapClickListener {

    /**
     * Static variables
     */
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 999;

    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final int CAMER_PERMISSION_REQUEST_CODE = 990;

    private static int POSITION;
    private static boolean POSITION_GOT = false;

    /**
     * Variables
     */

    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;
    private FloatingActionButton mLocationBtn;
    private FloatingActionButton mNavigationButton;
    private FloatingActionButton mCheckpointLocationBtn;
    private ProgressBar mRouteSearchBar;
    private TextView mRouteDistance;
    private TextView mRouteDuration;
    private TextView mCheckpointName;
    private TextView mCheckpointPoints;

    private ImageView mExpandImageView;
    private RelativeLayout mExpandableContainer;
    private LinearLayout mScanLayout;
    private static boolean expanded = false;

    private TextView mHintTextView;

    private FrameLayout mScanFrameLayout;
    ZXingScannerView scannerView;

    private View view;
    private View expandView;
    private LayoutInflater layoutInflater;

    /**
     * Additional variables
     */
    ArrayList<Polyline> polList;
    private static Location currentLocation;
    private static Checkpoint CHECKPOINT_FOR_ROUTE;

    private CheckpointClients checkpointClients;
    private UserClients userClients;
    private UpdateGeneralData updateGeneralData;
    //------------------------------------ Overrides -----------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        expandView = layoutInflater.inflate(R.layout.expandable_layout_map, null);

        initializeViews(view);

        if(MapObj.ROUTE_ENABLED) {
            mNavigationButton.setImageResource(R.drawable.ic_clear_black_24dp);
        }

        handleNavigationButton();
        handleCheckpointLocationButton();
        handleLocationButton();
        handleViewExpand();

        return view;
    }

    /**
     * Floating button handling methods
     */

    public void handleNavigationButton() {
        mNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if(CHECKPOINT_FOR_ROUTE != null && !MapObj.ROUTE_ENABLED) {
                        routeAlert("Set new route?");
                        MapObj.ROUTE_ENABLED = true;
                    } else if(CHECKPOINT_FOR_ROUTE != null && MapObj.ROUTE_ENABLED) {
                        routeAlert("Delete route?");
                        MapObj.ROUTE_ENABLED = false;
                    }
                } else {
                    networkAlert();
                }
            }
        });
    }

    public void handleCheckpointLocationButton() {
        mCheckpointLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(POSITION_GOT) {
                    if(MapObj.ROUTE_ENABLED) {
                        moveCamera(new LatLng(CHECKPOINT_FOR_ROUTE.getLatitude(), CHECKPOINT_FOR_ROUTE.getLongitude()), MapObj.DEFAULT_ZOOM);
                    } else {
                        setClosestCheckpointInfo();
                        moveCamera(new LatLng(CHECKPOINT_FOR_ROUTE.getLatitude(), CHECKPOINT_FOR_ROUTE.getLongitude()), MapObj.DEFAULT_ZOOM);
                    }
                }
            }
        });
    }

    public void handleLocationButton() {
        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    locationServiceCheck();
                } else {
                    networkAlert();
                }
            }
        });
    }

    public void handleViewExpand() {
        mExpandImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!expanded) {
                    onExpand(expandView);
                    expanded = true;
                } else if(expanded) {
                    onCollapse(expandView);
                    expanded = false;
                }
                mScanLayout.setOnClickListener(new View.OnClickListener() {
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
    }


    public void initializeViews(View view) {
        mLocationBtn = view.findViewById(R.id.myLocationBtn);
        mNavigationButton = view.findViewById(R.id.navigation_button);
        mRouteSearchBar = view.findViewById(R.id.routeSearchProgressBar);
        mRouteDistance = view.findViewById(R.id.checkpoint_distance);
        mRouteDuration = view.findViewById(R.id.checkpoint_time);
        mCheckpointName = view.findViewById(R.id.checkpoint_name);
        mCheckpointPoints = view.findViewById(R.id.checkpoint_points);
        mCheckpointLocationBtn = view.findViewById(R.id.checkpointLocationBtn);
        mExpandImageView = view.findViewById(R.id.expand_imageView);
        mExpandableContainer = view.findViewById(R.id.expandable_container);
        mScanFrameLayout = view.findViewById(R.id.scanView_container);

        mRouteSearchBar.setVisibility(View.INVISIBLE);
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
        getLocationPermission();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isNetworkAvailable()) {
            updateAlert();
            locationServiceCheck();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }



    @Override
    public void onCameraMove() {
        onCollapse(expandView);
    }

    /**
     * Closest checkpoint management
     */

    //putting existing checkpoints on map
    public void setCheckpointsOnMap() {
        for (Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(checkpoint.getName())
                    .position(new LatLng(checkpoint.getLatitude(), checkpoint.getLongitude()));

            for(UserCheckpoint userCheckpoint : CheckpointObj.userCheckpointArrayList) {
                if(userCheckpoint.getCheckpointID() == checkpoint.getId()) {
                    if(userCheckpoint.isCompleted()) {
                        markerOptions.icon(getMarkerIconColor("#00796b"));
                    } else {
                        markerOptions.icon(getMarkerIconColor("#e57373"));
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
                    checkpoint.getLatitude() + "," + checkpoint.getLongitude()+
                    RouteObj.KEY_URI;

            uriArray[i] = uri;
        }
        closestCheckpointFinderTask.execute(uriArray);
    }

    //finding which of all checkpoints is the closest one
    public int getClosestCheckpoint() {
        int closestPosition = 0;

        if(CheckpointObj.CLOSEST_CHECKPOINTS.size() > 0) {
            int closestDistance = CheckpointObj.CLOSEST_CHECKPOINTS.get(0).ExactDistance;

            for(int i = 1; i < CheckpointObj.CLOSEST_CHECKPOINTS.size(); i++) {

                if(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ExactDistance < closestDistance) {
                    closestDistance = CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ExactDistance;
                    closestPosition = i;
                }
            }
        }
        return closestPosition;
    }

    //setting info on the bar about checked checkpoint
    public void setClosestCheckpointInfo() {
        if(CheckpointObj.checkpointArrayList.size() > 0) {
            POSITION = getClosestCheckpoint();

            Checkpoint checkpoint = CheckpointObj.checkpointArrayList.get(POSITION);
            CHECKPOINT_FOR_ROUTE = new Checkpoint();
            CHECKPOINT_FOR_ROUTE = checkpoint;

            mCheckpointName.setText(checkpoint.getName());
            mCheckpointPoints.setText(Float.toString(checkpoint.getPoints()) + " points");

            if(CheckpointObj.CLOSEST_CHECKPOINTS.get(POSITION).ExactDistance < 1000) {
                mRouteDistance.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(POSITION).ExactDistance + " m");
            } else {
                mRouteDistance.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(POSITION).ApproximateDistance);
            }

            mRouteDuration.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(POSITION).TravelTime);
            POSITION_GOT = true;

            mRouteSearchBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Route management
     * @param lat
     * @param lng
     */

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

    /**
     * Map initalization and management
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        setCheckpointsOnMap();
    }

    public void locationAlert() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setMessage("Vietos paslauga bus reikalinga, norint naudotis programėle");
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

    //Checks for location permission and locates an user if the permission is granted
    public void locationServiceCheck() {
        if(mLocationPermissionGranted) {
            getDeviceLocation();

            if(currentLocation != null) {
                if(!MapObj.ROUTE_ENABLED) {
                    getCheckpointDistances(currentLocation);
                }
            }
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if(mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            if(checkAPILevel()) {
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                    locationAlert();
                } else {
                    getLocationPermission();
                }
            } else {
                getLocationPermission();
            }
            if(currentLocation != null) {
                if(!MapObj.ROUTE_ENABLED) {
                    getCheckpointDistances(currentLocation);
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(CheckpointObj.CLOSEST_CHECKPOINTS.size() > 0) {
            for(int i = 0; i < CheckpointObj.checkpointArrayList.size(); i++) {
                Checkpoint checkpoint = CheckpointObj.checkpointArrayList.get(i);
                if(checkpoint.getName().equals(marker.getTitle())) {
                    mCheckpointName.setText(checkpoint.getName());
                    mCheckpointPoints.setText(Float.toString(checkpoint.getPoints()) + " points");

                    if(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ExactDistance < 1000) {
                        mRouteDistance.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ExactDistance + " m");
                    } else {
                        mRouteDistance.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).ApproximateDistance);
                    }

                    mRouteDuration.setText(CheckpointObj.CLOSEST_CHECKPOINTS.get(i).TravelTime);

                    CHECKPOINT_FOR_ROUTE = new Checkpoint();
                    CHECKPOINT_FOR_ROUTE = checkpoint;

                    break;
                }
            }
        } else {
            for(Checkpoint checkpoint : CheckpointObj.checkpointArrayList) {
                if(checkpoint.getName().equals(marker.getTitle())) {
                    mCheckpointName.setText(checkpoint.getName());
                    mCheckpointPoints.setText(Float.toString(checkpoint.getPoints()) + " taškai");

                    mRouteDistance.setText("N/A");
                    mRouteDuration.setText("N/A");

                    CHECKPOINT_FOR_ROUTE = new Checkpoint();
                    CHECKPOINT_FOR_ROUTE = checkpoint;

                    break;
                }
            }
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        onCollapse(expandView);
    }

    //Initializes map
    private void initializeMap() {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    //Gets device location
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

                            if(!MapObj.ROUTE_ENABLED) {
                                getCheckpointDistances(currentLocation);
                            }
                        } else {
                            Toast.makeText(getContext(), "Nebuvo įmanoma nustatyi buvimo vietos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e) {
        }
    }

    //Gets location permission if it's not granted
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initializeMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //Location permission request result handler
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

    /**
     * Camera movement
     * @param latLng
     * @param zoom
     */

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * Expandable view management
     * @param expandView
     */

    //Expands top view when clicked
    public void onExpand(View expandView) {
        mHintTextView = expandView.findViewById(R.id.checkpoint_hint);
        mScanLayout = expandView.findViewById(R.id.scan_layout);
        mExpandImageView.setImageResource(R.drawable.ic_expand_less_black_24dp);
        mScanLayout.setVisibility(View.VISIBLE);

        if(CHECKPOINT_FOR_ROUTE != null) {
            mHintTextView.setText(CHECKPOINT_FOR_ROUTE.getHint());
            for(UserCheckpoint userCheckpoint : CheckpointObj.userCheckpointArrayList) {
                if(userCheckpoint.getCheckpointID() == CHECKPOINT_FOR_ROUTE.getId()) {
                    if(userCheckpoint.isCompleted()) {
                        mScanLayout.setVisibility(View.INVISIBLE);

                        break;
                    } else {
                        mScanLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            mHintTextView.setText("Loading..");
        }
        mExpandableContainer.addView(expandView);
    }

    //Colapses expanded view
    public void onCollapse(View expandView) {
        mExpandableContainer.removeView(expandView);
        mExpandImageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
    }

    /**
     * Scan view management
     */

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

    //Checks if camera permission is granted and opens camera if it's granted
    public void cameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA};
        if(ContextCompat.checkSelfPermission(getContext(), CAMERA_PERMISSION ) == PackageManager.PERMISSION_GRANTED) {
            scannerView.startCamera();
            ActivityObj.IS_SCAN_CAMERA = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, CAMER_PERMISSION_REQUEST_CODE);
            if(ContextCompat.checkSelfPermission(getContext(), CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                scannerView.startCamera();
                ActivityObj.IS_SCAN_CAMERA = true;
            }
        }
    }

    public void onPostScanned(Result result) {
        String scanResult = result.getText();

        if(scanResult.equals(CHECKPOINT_FOR_ROUTE.getScan())) {
            onPostScanAlert("Checkpoint completed");
            for(UserCheckpoint userCheckpoint : CheckpointObj.userCheckpointArrayList) {

                if(CHECKPOINT_FOR_ROUTE.getId() == userCheckpoint.getCheckpointID()) {
                    userCheckpoint.setCompleted(true);
                    UserObj.USER.setPoints(UserObj.USER.getPoints() + CHECKPOINT_FOR_ROUTE.getPoints());

                    if(isNetworkAvailable()) {
                        checkpointClients = new CheckpointClients(getContext());
                        userClients = new UserClients(getContext());

                        checkpointClients.updateUserCheckpoint(userCheckpoint);
                        userClients.updateUser(UserObj.USER);
                    }
                    CHECKPOINT_FOR_ROUTE = null;

                    break;
                }
            }
            setCheckpointsOnMap();
        } else {
            onPostScanAlert("Wrong checkpoint");
        }
    }

    public void onPostScanAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Updating general data
     */

    public void onUpdate() {
        if(isNetworkAvailable()) {
            updateGeneralData = new UpdateGeneralData(getContext());
            updateGeneralData.getAppInfo();
            updateGeneralData.getCheckpointList();
            updateGeneralData.updateUserCheckpoints(CheckpointObj.userCheckpointArrayList);
        }
    }

    /**
     * Alerts
     * @param message
     */

    public void routeAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(MapObj.ROUTE_ENABLED) {
                    getRouteInfo(CHECKPOINT_FOR_ROUTE.getLatitude(), CHECKPOINT_FOR_ROUTE.getLongitude());
                    mNavigationButton.setImageResource(R.drawable.ic_clear_black_24dp);
                    MapObj.DEFAULT_ZOOM = 18f;
                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), MapObj.DEFAULT_ZOOM);
                } else {
                    deleteRoute();
                    mNavigationButton.setImageResource(R.drawable.ic_navigation_black_24dp);
                    MapObj.DEFAULT_ZOOM = 15f;
                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), MapObj.DEFAULT_ZOOM);
                }
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

    public void updateAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Update info?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onUpdate();
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

    /**
     * Network alerts
     * @return
     */
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
