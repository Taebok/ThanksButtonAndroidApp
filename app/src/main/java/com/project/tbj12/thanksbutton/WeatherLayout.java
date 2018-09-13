package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

class WeatherLayout extends LinearLayout implements View.OnClickListener {
    private final Context context;
    private MyWeatherTableLayout weatherBody;

    public WeatherLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("onFinishedInflate", "Called WeatherLayout onFinishedInflate");

        final boolean switchStatus = context.getResources().getBoolean(R.bool.weather_switch_status);

        Switch checkedWeatherSwitch = findViewById(R.id.weather_switch);
        weatherBody = this.findViewById(R.id.weather_body);
        final TextView textViewForHiddenWeatherContent = findViewById(R.id.weather_switch_off_message);

        checkedWeatherSwitch.setChecked(switchStatus);
        OnSwitchListener onSwitchListener = new OnSwitchListener(checkedWeatherSwitch, switchStatus) {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Switch Toggle", "Switch Toggle : " + isChecked);

                if (!isChecked) {
                    switchViewVisibility(textViewForHiddenWeatherContent, weatherBody);
                } else {
                    switchViewVisibility(weatherBody, textViewForHiddenWeatherContent);
                }
            }
        };

        checkedWeatherSwitch.setOnCheckedChangeListener(onSwitchListener);
    }

    @Override
    public void onClick(View v) {
        EditText cityName = findViewById(R.id.weather_city_name);
        if (cityName.getText().toString().isEmpty()) {
            return;
        }
        String textCityName = cityName.getText().toString().trim();

        weatherBody.getWeatherFromCityName(textCityName);
    }
}
