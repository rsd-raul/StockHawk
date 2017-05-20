package com.udacity.stockhawk.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.stockhawk.R;

public class DetailActivity extends AppCompatActivity {

    private String[] mLabels = {"Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep", "Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep", "Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep"};
    private float[] mValues = {900.5f, 920.4f, 890.3f, 960f, 975.2f, 990.2f, 950f, 930f, 950.9f, 900.5f, 920.4f, 890.3f, 960f, 975.2f, 990.2f, 950f, 930f, 950.9f, 900.5f, 920.4f, 890.3f, 960f, 975.2f, 990.2f, 950f, 930f, 950.9f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        new LinearGraph(this).show(mLabels, mValues);
    }
}