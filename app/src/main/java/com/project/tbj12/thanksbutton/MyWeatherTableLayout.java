package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TableLayout;

class MyWeatherTableLayout extends TableLayout {

    public MyWeatherTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("onSizeChanged", "@@@@ Call MyWeatherTableLayout onSizeChanged method");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("onDraw", "@@@@ Call MyWeatherTableLayout onDraw method");
        super.onDraw(canvas);
    }

    // Get Weather Information With the Text of the City Name.
    void getWeatherFromCityName(String textCityName) {

    }
}
