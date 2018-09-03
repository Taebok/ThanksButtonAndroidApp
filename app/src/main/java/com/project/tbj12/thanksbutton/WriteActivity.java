package com.project.tbj12.thanksbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class WriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Log.d("Write Activity", "~~~~~~~~~~~~~~~~~~~Called Write Activity");
    }

}
