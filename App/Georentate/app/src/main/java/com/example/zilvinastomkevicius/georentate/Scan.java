package com.example.zilvinastomkevicius.georentate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.zilvinastomkevicius.georentate.Activites.MainActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scan {

    private View mView;
    private Context mContext;
    private Activity mActivity;
    RelativeLayout mScanView;

    ZXingScannerView scannerView;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final int CAMER_PERMISSION_REQUEST_CODE = 990;

    public Scan(View view, Context context, Activity activity) {
        mView = view;
        mContext = context;
        mActivity = activity;
    }

    public void startCamera() {
        scannerView = new ZXingScannerView(mContext);
        mScanView = mView.findViewById(R.id.scan_view);
        mScanView.addView(scannerView);

        scannerView.setResultHandler(new Scan.ZXingScannerResultHandler());
        cameraPermission();
    }

    public void stopCamera() {
        scannerView.stopCamera();
    }

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {

        @Override
        public void handleResult(Result result) {
          //  SharingObjects.SCANNER_STRING = result.getText();
            stopCamera();
        }
    }

    public void cameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(mContext, CAMERA_PERMISSION ) == PackageManager.PERMISSION_GRANTED) {
            scannerView.startCamera();
        }

        else {
            ActivityCompat.requestPermissions(mActivity, permissions, CAMER_PERMISSION_REQUEST_CODE);
        }
    }
}
