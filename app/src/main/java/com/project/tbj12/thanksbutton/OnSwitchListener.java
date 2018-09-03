package com.project.tbj12.thanksbutton;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

class OnSwitchListener implements CompoundButton.OnCheckedChangeListener {
    private View contentBody;
    private View textViewForHiddenContentBody;

    OnSwitchListener(View contentBody, View textViewForHiddenContentBody) {
        this.contentBody = contentBody;
        this.textViewForHiddenContentBody = textViewForHiddenContentBody;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("Switch Toggle", "Switch Toggle : " + isChecked);
        if (!isChecked) {
            switchViewVisibility(textViewForHiddenContentBody, contentBody);
        } else {
            switchViewVisibility(contentBody, textViewForHiddenContentBody);
        }
    }

    private void switchViewVisibility(View visibleView, View invisibleView) {
        visibleView.setVisibility(View.VISIBLE);
        invisibleView.setVisibility(View.GONE);
    }
}
