package com.project.tbj12.thanksbutton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

public class MyLocationMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener {
    private static final int REQUEST_CODE_ACCESS_LOCATION = 1;
    private boolean isLocationPermissionGranted;
    private Context context;
    private GoogleMap map;
    private boolean isMapReady;
    //    private LocationManager manager;
    private Location currentLocation;
    private MarkerOptions markerOption;
    private OnChangedLocationListener changedLocationListener;
    //    private String locationProvider;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Prevent fragment created again.
        setRetainInstance(true);
        context = getContext();

//        if (context != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
//            manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        }
//
//        locationProvider = manager.getBestProvider(new Criteria(), true);
//        Log.d("GetBestLocationProvider", "----------" + locationProvider);

        // 위치정보 서비스가 비활성된 경우 provider는 "passive"
        // showSettingDialog()에서 사용자 서비스 설정 요청하기
//        if (locationProvider.equals("passive")) {
//            Toast.makeText(context, R.string.location_not_service_message, Toast.LENGTH_SHORT).show();
//            showSettingDialog();
//        }

        // Get FusedLocationProviderClient for getting my location
        if (context != null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

        if (markerOption == null) {
            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker());
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // When parent layout's switch is on, statusContentBody is true
        getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentOnResume", "========= onResume ==========");

        checkPermission();
        updateLocationUI();

        getCurrentLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FragmentOnPause", "############### onPause ###############");
        isMapReady = false;
    }

    // Set marker and call onChangedLocationWithAddress method.
    // Get the last known currentLocation.
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d("Location Service", String.format("Latitude: %f, Longitude: %f", location.getLatitude(), location.getLongitude()));

                    onLocationChanged(location);
                }
            }
        });
    }

    // Set position of new marker or existed marker
    void moveMarker(LatLng latLng) {
//        if (markerOption == null) {
//            markerOption = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker());
//        } else {
//            markerOption.position(latLng);
//        }
        markerOption.position(latLng);
        map.clear();
        map.addMarker(markerOption);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    // Get marker position's an address and a lat&lng and call ChangedLocationListener's method.
    void callOnChangedLocation(String address, LatLng latLng) {
        if (changedLocationListener != null) {
            changedLocationListener.onChangedLocationWithAddress(address);
            changedLocationListener.onChangedLocationWithLatLng(latLng);
        }
    }

    void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            isLocationPermissionGranted = false;
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 필요한 권한에 대한 목적과 사용범위를 설명해야 합니다.
                // 방법은 dialog 형태의 안내문구를 보이고, 원한다면 권한 설정 화면으로 이동합니다.

                Toast.makeText(context, getString(R.string.location_permission_not_allowed), Toast.LENGTH_SHORT).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ACCESS_LOCATION);
            }

        } else {
            // permissions were allowed.
            isLocationPermissionGranted = true;
            Toast.makeText(context, getString(R.string.location_permission_allowed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ACCESS_LOCATION:  // permission for device currentLocation
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        isLocationPermissionGranted = false;
                        updateLocationUI();
                        return;
                    }
                }
                isLocationPermissionGranted = true;
                updateLocationUI();
                break;
        }
    }

    // If permission is granted, set google map UI.
    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        if (isLocationPermissionGranted) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }

        // If the map was ready, it's true.
        isMapReady = true;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getCurrentLocation();
        return false;
    }

    // If call getMapAsync(), call OnMapReadyCallback method.
    // Asynchronously get map data.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Google Map", "GoogleMap Service~~~~~~~~~~~~~");
        map = googleMap;

        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            Log.e("Google Map", "GoogleMap Initialiazer Error!!!!!!", e);
        }

        map.setOnMyLocationButtonClickListener(this);
        map.setOnMapClickListener(this);
        map.getUiSettings().setMapToolbarEnabled(false);

        updateLocationUI();

    }

    // Set directly a currentLocation on map.
    @Override
    public void onMapClick(LatLng latLng) {
        Location location = new Location(LocationManager.PASSIVE_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);

        onLocationChanged(location);
    }

    public void onLocationChanged(Location location) {
        if (currentLocation != null) {
            currentLocation.reset();
        }
        currentLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveMarker(latLng);

        String address = getAddressAtCurrentLocation();
        callOnChangedLocation(address, latLng);
    }

    // Pass an address to LocationLayout.class
    String getAddressAtCurrentLocation() {
        StringBuilder resultMessage = new StringBuilder();

        if (currentLocation != null) {
            Locale locale = Locale.getDefault();
            Geocoder geocoder = new Geocoder(context, locale);

            try {
                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);

                if (addresses != null) {
                    if (addresses.isEmpty()) {
                        resultMessage.append(getString(R.string.location_none_address_message));
                    } else {
                        for (int i = 0; i < addresses.size(); i++) {
                            resultMessage.append(addresses.get(0).getAddressLine(i));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            resultMessage.append(getString(R.string.location_none_address_message));
        }
        Log.d("Get Address", resultMessage.toString());

        return resultMessage.toString();
    }

    // If it can find location from address, display marker on map
    // This method can be called from outside. At that time, isMapReady must be true.
    void getLocationFromAddress(String address) {
        if (!isMapReady) {
            return;
        }

        Locale locale = Locale.getDefault();
        Geocoder geocoder = new Geocoder(context, locale);
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null) {
                if (addressList.isEmpty()) {
                    Toast.makeText(context, R.string.location_none_location_message, Toast.LENGTH_SHORT).show();
                } else {
                    Location location = new Location(LocationManager.PASSIVE_PROVIDER);
                    location.setLatitude(addressList.get(0).getLatitude());
                    location.setLongitude(addressList.get(0).getLongitude());

                    Log.d("GetLocationFromAddress", MessageFormat.format("Latitude: {0}, " +
                            "Longitude: {1}", location.getLatitude(), location.getLongitude()));

                    onLocationChanged(location);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setChangedLocationListener(OnChangedLocationListener listener) {
        this.changedLocationListener = listener;
    }

    // location service setting
    void showSettingDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.location_setting_dialog_title);
        alertDialog.setMessage(R.string.location_setting_dialog_message);

        alertDialog.setPositiveButton("지금 설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("나중에", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public boolean isMapReady() {
        return isMapReady;
    }

    interface OnChangedLocationListener {
        void onChangedLocationWithAddress(String address);

        void onChangedLocationWithLatLng(LatLng currentLatLng);
    }
}