package com.example.simplecountdown;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.transition.Fade;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(0, 0);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        final LinearLayout backgroundLayout = findViewById(R.id.backgroundLayout);
        final FloatingActionButton clockButton = findViewById(R.id.clockButton);
        final SeekBar timeSlider = findViewById(R.id.timeSlider);
        final TextView ctMinutes = findViewById(R.id.ctMinutes);

        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimerActivity2.class);
                intent.putExtra(TimerActivity2.SELECTED_MINUTES, ctMinutes.getText());

                Pair<View, String> el1 = Pair.create(clockButton, "clock_button_tran");
                Pair<View, String> el2 = Pair.create(ctMinutes, "ct_minutes_tran");
                Pair<View, String> el3 = Pair.create(backgroundLayout, "background_layout_trans");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this, el1, el2, el3);
                startActivity(intent, options.toBundle());

            }
        });

        timeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ctMinutes.setText(String.valueOf(i/5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
}