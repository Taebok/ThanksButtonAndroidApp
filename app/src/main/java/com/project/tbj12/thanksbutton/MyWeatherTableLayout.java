package com.project.tbj12.thanksbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

class MyWeatherTableLayout extends TableLayout {
    private boolean statusContentBody;

    public MyWeatherTableLayout(Context context) {
        super(context);
    }

    public MyWeatherTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStatusContentBody(boolean statusContentBody) {
        this.statusContentBody = statusContentBody;
    }
}
