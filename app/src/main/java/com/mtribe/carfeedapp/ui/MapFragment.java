package com.mtribe.carfeedapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mtribe.carfeedapp.BackgroundJobExecutor;
import com.mtribe.carfeedapp.R;
import com.mtribe.carfeedapp.databinding.MapFragmentBinding;
import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;
import com.mtribe.carfeedapp.model.CarInformation;
import com.mtribe.carfeedapp.viewmodel.CarInformationListViewModel;

import java.util.List;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private static final String TAG = "MapFragment";
    private List<? extends CarInformation> mCarInformationList;

    private MapFragmentBinding mBinding;

    private final LatLng mDefaultLocation = new LatLng(53.558, 9.927);

    private GoogleMap mMap;

    private static final int DEFAULT_ZOOM = 15;

    private static final int CURRENT_LOCATION_MARKER_INDEX = -1;

    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private SparseArray<Marker> mMarkerArray = new SparseArray<>();;

    //Bug in marker.isInfoWindowShown() API. It returns false most of the time.
    //Using this variable to manage the toggle states of infowindow of the marker clicked..
    private boolean isInfoWindowShown   = false;

    //using this to override onMapClick event so that toggling makers and info window doean't break.
    private Marker mLastClickedMarker;

    private BackgroundJobExecutor bgExecutor = new BackgroundJobExecutor();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CarInformationListViewModel viewModel = ViewModelProviders.of(this).get(CarInformationListViewModel.class);
        subscribeUi(viewModel);
    }

    private void subscribeUi(CarInformationListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getCarInformationEntities().observe(this, new Observer<List<CarInformationEntity>>() {
            @Override
            public void onChanged(@Nullable List<CarInformationEntity> carInformationEntityList) {
                if (carInformationEntityList != null) {
                    mBinding.setIsLoading(false);
                    setCarInformationList(carInformationEntityList);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    public void setCarInformationList(List<? extends CarInformation > carInformationList) {
        if(mCarInformationList == null) {
            mCarInformationList = carInformationList;
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mCarInformationList.size();
                }

                @Override
                public int getNewListSize() {
                    return mCarInformationList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mCarInformationList.get(oldItemPosition).getId() ==
                            mCarInformationList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    CarInformation newCarInformation = mCarInformationList.get(newItemPosition);
                    CarInformation oldCarInformation = mCarInformationList.get(oldItemPosition);
                    return newCarInformation.getId() == oldCarInformation.getId()
                            && newCarInformation.getVin().equals(oldCarInformation.getVin());
                }
            });
            mCarInformationList = carInformationList;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        MainActivity mainActivity = ((MainActivity) getActivity());
        if(mainActivity == null)
            return;
        // Prompt the user for permission.
        ((MainActivity) getActivity()).getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        //Run on backgroung thread as it has to loop large number of items.
        bgExecutor.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                populateMarkers(googleMap);
            }
        });
    }

    private void populateMarkers(GoogleMap googleMap) {
        for(CarInformation carInfo : mCarInformationList) {
            LatLng latLon = new LatLng(carInfo.getLatitude(), carInfo.getLongitude());
            //Run on mainThread thread as it has to update the .markers on the google map.
            bgExecutor.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latLon).title(carInfo.getName()));
                    marker.setTag(carInfo.getId());
                    mMarkerArray.put(Integer.parseInt(marker.getTag().toString()), marker);
                }
            });
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Marker marker;
                        if (task.isSuccessful()) {

                            mLastKnownLocation = task.getResult();
                            if(mLastKnownLocation != null){
                                // Set the map's camera position to the current location of the device.
                                LatLng currentLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                marker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                marker.setTag(CURRENT_LOCATION_MARKER_INDEX);
                                mMarkerArray.put(Integer.parseInt(marker.getTag().toString()), marker);
                                // ENABLE after Testing only for testing purposes
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            } else {
                                setDefaultLocation();
                            }
                        } else {
                            setDefaultLocation();
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void setDefaultLocation() {
        Log.d(TAG, "Current location is null. Using defaults.");
        Marker marker = mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("Default Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        marker.setTag(CURRENT_LOCATION_MARKER_INDEX);
        mMarkerArray.put(Integer.parseInt(marker.getTag().toString()), marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }
    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    protected void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                ((MainActivity) getActivity()).getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected void setLocationPermissionGranted(boolean isGranted) {
        mLocationPermissionGranted = isGranted;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.mLastClickedMarker = marker;
        if(!isInfoWindowShown) {
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            isInfoWindowShown = true;
        } else {
            marker.hideInfoWindow();
            isInfoWindowShown = false;
        }
        toggleOthermarkers(marker);
        return true;
    }

    private void toggleOthermarkers(Marker marker) {
        for(int i = 0; i < mMarkerArray.size(); i++) {
            int key = mMarkerArray.keyAt(i);
            if(key != Integer.parseInt(marker.getTag().toString())) {
                Marker otherMarker = mMarkerArray.get(key);
                otherMarker.setVisible(!isInfoWindowShown);
            }
        }
    }

    //Overriding this event to stop event from progating to onMarkerClick which disprupts the smooth toggling.
    //So invoking the onMarkerClick with last clicked ,marker.
    @Override
    public void onMapClick(LatLng latLng) {
        if(mLastClickedMarker != null) {
            onMarkerClick(mLastClickedMarker);
        }
    }
}
