package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

class LocationLayout extends LinearLayout implements View.OnClickListener {
    private boolean switchStatus;
    private final Context context;
    private MyLocationMapFragment mapFragment;
    private Handler locationHandler = new Handler();

    public LocationLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // Switch's default status value gets from ../values/default_value.xml
        switchStatus = context.getResources().getBoolean(R.bool.location_switch_status);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("onFinishedInflate", "Called LocationLayout onFinishedInflate");

        // Set switch's changed listener.
        Switch checkedLocationSwitch = findViewById(R.id.location_switch);
        final LinearLayout locationBody = findViewById(R.id.location_body);
        final TextView textViewForHiddenLocationContent = findViewById(R.id.location_switch_off_message);

        mapFragment = (MyLocationMapFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.location_map);

        checkedLocationSwitch.setChecked(switchStatus);
        OnSwitchListener onSwitchListener = new OnSwitchListener(checkedLocationSwitch, switchStatus) {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Switch Toggle", "Switch Toggle : " + isChecked);

                if (!isChecked) {
                    switchViewVisibility(textViewForHiddenLocationContent, locationBody);
                    if (mapFragment.isAdded())
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                                .detach(mapFragment).commit();
                } else {
                    switchViewVisibility(locationBody, textViewForHiddenLocationContent);
                    if (mapFragment.isDetached())
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                                .attach(mapFragment).commit();
                    mapFragment.onResume();
                }
            }
        };

        checkedLocationSwitch.setOnCheckedChangeListener(onSwitchListener);

        setListenerOnMapFragment();

        // 주소입력 후 위치정보 찾기 버튼 클릭 리스너 지정
        Button addressInputButton = findViewById(R.id.location_address_request_button);
        addressInputButton.setOnClickListener(this);

        // The button set a clickable state that can request for a location with the text widget
        if (mapFragment.isMapReady())
            addressInputButton.setClickable(true);
    }

    // Set ChangedLocationListener in MyLocationMapFragment class
    private void setListenerOnMapFragment() {

        final EditText locationAddress = findViewById(R.id.location_address);

        if (mapFragment != null) {

            // Set implementation of ChangedLocationListener in MyLocationMapFragment class
            mapFragment.setChangedLocationListener(new MyLocationMapFragment.OnChangedLocationListener() {
                @Override
                public void onChangedLocationWithAddress(final String address) {
                    // Get the address in a current location of the device and set value on text
                    // widget.
//                    locationAddress.setText(address);
                    locationHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            locationAddress.setText(address);
                        }
                    });
                }

                // Request for a weather information with above an address or a location
                // information.
                @Override
                public void onChangedLocationWithLatLng(LatLng currentLatLng) {

                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        EditText locationAddress = findViewById(R.id.location_address);
        if (locationAddress.getText().toString().isEmpty()) {
            return;
        }

        String textAddress = locationAddress.getText().toString().trim();

        mapFragment.getLocationFromAddress(textAddress);
    }

}