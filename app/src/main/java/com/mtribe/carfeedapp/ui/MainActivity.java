package com.mtribe.carfeedapp.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.mtribe.carfeedapp.R;

public class MainActivity extends AppCompatActivity{
    private boolean mLocationPermissionGranted;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Add car info list fragment if this is first creation
        if (savedInstanceState == null) {
            CarInformationListFragment fragment = new CarInformationListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, CarInformationListFragment.TAG).commit();
        }
    }

    public void showMap() {
        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().addToBackStack("mapfragment").replace(R.id.fragment_container, mapFragment, "mapfragment").commit();
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    protected void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("mapfragment");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapFragment.setLocationPermissionGranted(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("mapfragment");
        mapFragment.setLocationPermissionGranted(false);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapFragment.setLocationPermissionGranted(true);
                }
            }
        }
        mapFragment.updateLocationUI();
    }
}
