package com.example.simplecountdown;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.transition.Fade;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimerActivity extends AppCompatActivity {

    public static String SELECTED_MINUTES = "selected_minutes";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Fade fade = new Fade();
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        //**** Init variables ****//
        String stringMinutes = "0";

        //**** Getting elements ****//
        final LinearLayout backgroundLayout = findViewById(R.id.backgroundLayout);
        final FloatingActionButton clockButton = findViewById(R.id.clockButton);
        final TextView ctMinutes = findViewById(R.id.ctMinutes);
        final TextView ctSeconds = findViewById(R.id.ct_seconds);

        //**** Getting values from previous activity ****//
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            stringMinutes = extras.getString(SELECTED_MINUTES);
        }

        ctMinutes.setText(formatTime(Long.valueOf(stringMinutes)));

        Rect r = new Rect(1,100,100,1);

        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerActivity.this, MainActivity.class);

                Pair<View, String> el1 = Pair.create(clockButton, "clock_button_tran");
                Pair<View, String> el2 = Pair.create(ctMinutes, "ct_minutes_tran");
                Pair<View, String> el3 = Pair.create(backgroundLayout, "background_layout_trans");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        TimerActivity.this, el1, el2, el3);
                startActivity(intent, options.toBundle());
            }
        });

        Long minutesInMills = Long.parseLong(stringMinutes) * 60 * 1000;

        new CountDownTimer(minutesInMills, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) % 60;

                ctMinutes.setText(formatTime(minutes));
                ctSeconds.setText(formatTime(seconds));
            }

            public void onFinish() {
                ctMinutes.setText("ST");
                ctSeconds.setText("OP");
                cancel();
            }
        }.start();

    }

    private String formatTime(Long value) {
        if (value < 10) {
            return (String.format("0%d", value));
        } else {
            return (String.valueOf(value));
        }
    }

}