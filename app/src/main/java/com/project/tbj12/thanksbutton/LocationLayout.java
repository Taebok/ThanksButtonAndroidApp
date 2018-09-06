package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

class LocationLayout extends LinearLayout implements View.OnClickListener {
    static boolean switchStatus;
    private final Context context;
    private OnRequestedForLocationListener onRequestedForLocationListener;
    private Fragment mapFragment;

    public LocationLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // switch 상태와 그에 따른 view의 visible value를 어떻게 유지 할까?
        // Switch's default status value gets from ../values/default_value.xml
        switchStatus = context.getResources().getBoolean(R.bool.location_switch_status);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("onFinishedInflate", "Called onFinishedInflate");
        this.requestFocus();

        // Set switch's changed listener.
        Switch checkedLocationSwitch = findViewById(R.id.location_switch);
        final LinearLayout locationBody = findViewById(R.id.location_body);
        final TextView textViewForHiddenLocationContent = findViewById(R.id.location_switch_off_message);

        mapFragment = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById
                (R.id.location_map);

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
        Button addressInputButton = findViewById(R.id.location_address_input_button);
        addressInputButton.setOnClickListener(this);

        if (onRequestedForLocationListener != null)
            addressInputButton.setClickable(true);
    }

    // Set ChangedLocationListener in MyLocationMapFragment class
    private void setListenerOnMapFragment() {

        final EditText locationAddress = findViewById(R.id.location_address);

        if (mapFragment instanceof MyLocationMapFragment) {
            // Set ChangedLocationListener in MyLocationMapFragment class
            ((MyLocationMapFragment) mapFragment).setChangedLocationListener(new MyLocationMapFragment.OnChangedLocationListener() {
                @Override
                public void onChangedLocation(String address) {
                    locationAddress.setText(address);
                }
            });
            // Get OnRequestForLocationListener from method of MyLocationMapFragment
            onRequestedForLocationListener = ((MyLocationMapFragment) mapFragment)
                    .getListenerOnLocationLayout();
        }
    }

    @Override
    public void onClick(View v) {
        EditText locationAddress = findViewById(R.id.location_address);
        if (locationAddress.getText().toString().isEmpty()) {
            return;
        }

        String textAddress = locationAddress.getText().toString().trim();

        if (onRequestedForLocationListener != null) {
            onRequestedForLocationListener.onRequestForLocation(textAddress);
        }
    }

    interface OnRequestedForLocationListener {
        void onRequestForLocation(String address);
    }
}