package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

class LocationLayout extends LinearLayout implements View.OnClickListener {
    private final Context context;
    private OnRequestedForLocationListener onRequestedForLocationListener;
    private boolean switchStatus;

    public LocationLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        switchStatus = context.getResources().getBoolean(R.bool.location_switch_status);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("onFinishedInflate", "Called onFinishedInflate");
        this.requestFocus();

        // Set switch's changed listener.
        // switch 상태와 그에 따른 view의 visible value를 어떻게 유지 할까?
        Switch checkedLocationSwitch = findViewById(R.id.location_switch);
        LinearLayout locationBody = findViewById(R.id.location_body);
        TextView textViewForHiddenLocationContent = findViewById(R.id.location_switch_off_message);

        OnSwitchListener onSwitchListener = new OnSwitchListener(locationBody,
                textViewForHiddenLocationContent);

        checkedLocationSwitch.setOnCheckedChangeListener(onSwitchListener);
        checkedLocationSwitch.setChecked(switchStatus);

        setListenerOnMapFragment();

        // 주소입력 후 위치정보 찾기 버튼 클릭 리스너 지정
        Button addressInputButton = findViewById(R.id.location_address_input_button);
        addressInputButton.setOnClickListener(this);

        if (onRequestedForLocationListener != null)
            addressInputButton.setClickable(true);
    }

    @Override
    public void onDescendantInvalidated(@NonNull View child, @NonNull View target) {
        super.onDescendantInvalidated(child, target);
    }

    // Set ChangedLocationListener in MyLocationMapFragment class
    private void setListenerOnMapFragment() {
        Fragment mapFragment = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById
                (R.id.location_map);
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