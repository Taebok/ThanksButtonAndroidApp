package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

class WeatherLayout extends LinearLayout {
    private TextView textViewForHiddenWeatherContent;
    private LayoutParams params;
    private Switch checkedWeatherSwitch;
    private TableLayout weatherBody;

    public WeatherLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        textViewForHiddenWeatherContent = new TextView(getContext());
        textViewForHiddenWeatherContent.setText(R.string.weather_switch_off_text);
        textViewForHiddenWeatherContent.setVisibility(View.GONE);

        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        checkedWeatherSwitch = findViewById(R.id.weather_switch);
        weatherBody = this.findViewById(R.id.weather_body);
        OnSwitchListener onSwitchListener = new OnSwitchListener(weatherBody, textViewForHiddenWeatherContent);

        this.addView(textViewForHiddenWeatherContent, params);

        checkedWeatherSwitch.setOnCheckedChangeListener(onSwitchListener);
        checkedWeatherSwitch.setChecked(true);
    }

}
