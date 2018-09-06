package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

class WeatherLayout extends LinearLayout {
    private final Context context;

    public WeatherLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final boolean switchStatus = context.getResources().getBoolean(R.bool.weather_switch_status);

        Switch checkedWeatherSwitch = findViewById(R.id.weather_switch);
        final TableLayout weatherBody = this.findViewById(R.id.weather_body);
        final TextView textViewForHiddenWeatherContent = findViewById(R.id.weather_switch_off_message);

        checkedWeatherSwitch.setChecked(switchStatus);
        OnSwitchListener onSwitchListener = new OnSwitchListener(checkedWeatherSwitch, switchStatus) {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Switch Toggle", "Switch Toggle : " + isChecked);

                if (isChecked) {
                    switchViewVisibility(weatherBody, textViewForHiddenWeatherContent);
                } else {
                    switchViewVisibility(textViewForHiddenWeatherContent, weatherBody);
                }
            }
        };

        checkedWeatherSwitch.setOnCheckedChangeListener(onSwitchListener);
    }

}
