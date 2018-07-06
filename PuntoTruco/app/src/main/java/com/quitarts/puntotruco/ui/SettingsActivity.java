package com.quitarts.puntotruco.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.quitarts.puntotruco.R;
import com.quitarts.puntotruco.Utils;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerPoints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);

        spinnerPoints = findViewById(R.id.settings_points_spinner);
        spinnerPoints.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int points = Utils.getPoints();
        String[] pointsArray = getResources().getStringArray(R.array.arrays_points);
        int index = getIndex(pointsArray, points);
        spinnerPoints.setSelection(index);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Utils.savePoints(Integer.parseInt((String) adapterView.getItemAtPosition(i)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private int getIndex(String[] pointsArray, int points) {
        for (int i = 0; i < pointsArray.length; i++) {
            if (Integer.parseInt(pointsArray[i]) == points)
                return i;
        }

        return 30;
    }
}