package com.project.tbj12.thanksbutton;

import android.view.View;
import android.widget.CompoundButton;

abstract class OnSwitchListener implements CompoundButton.OnCheckedChangeListener {

    OnSwitchListener(CompoundButton buttonView, boolean isChecked) {
        onCheckedChanged(buttonView, isChecked);
    }

    @Override
    public abstract void onCheckedChanged(CompoundButton buttonView, boolean isChecked);

    void switchViewVisibility(View visibleView, View invisibleView) {
        visibleView.setVisibility(View.VISIBLE);
        invisibleView.setVisibility(View.GONE);
    }
}
