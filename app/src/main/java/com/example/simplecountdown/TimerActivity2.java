package com.example.simplecountdown;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimerActivity2 extends AppCompatActivity {

    public static String SELECTED_MINUTES = "selected_minutes";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer2);

        Fade fade = new Fade();
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        //**** Init variables ****//
        String stringMinutes = "0";

        //**** Getting elements ****//
        final FrameLayout backgroundLayout = findViewById(R.id.backgroundLayout);
        final FloatingActionButton clockButton = findViewById(R.id.clockButton);
        final TextView ctMinutes = findViewById(R.id.ctMinutes);
        final TextView ctSeconds = findViewById(R.id.ct_seconds);
        final FrameLayout progressLayout = findViewById(R.id.progressLayout);

        //**** Getting values from previous activity ****//
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            stringMinutes = extras.getString(SELECTED_MINUTES);
        }

        ctMinutes.setText(formatTime(Long.valueOf(stringMinutes)));

        Rect r = new Rect(1,100,100,1);

        Long minutesInMills = Long.parseLong(stringMinutes) * 60 * 1000;

        CountDownTimer ct = new CountDownTimer(minutesInMills, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) % 60;
                long millsFromStart = minutesInMills - millisUntilFinished;
                long displayMaxHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
                float scale = (displayMaxHeight*millsFromStart)/minutesInMills;
                ctMinutes.setText(formatTime(minutes));
                ctSeconds.setText(formatTime(seconds));

                Log.d("", String.valueOf(scale));
                progressLayout.setMinimumHeight((int)(scale));
            }

            public void onFinish() {
                ctMinutes.setText("ST");
                ctSeconds.setText("OP");
                this.cancel();
            }
        }.start();

        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ct.cancel();

                Intent intent = new Intent(TimerActivity2.this, MainActivity.class);

                Pair<View, String> el1 = Pair.create(clockButton, "clock_button_tran");
                Pair<View, String> el2 = Pair.create(ctMinutes, "ct_minutes_tran");
                Pair<View, String> el3 = Pair.create(backgroundLayout, "background_layout_trans");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        TimerActivity2.this, el1, el2, el3);
                startActivity(intent, options.toBundle());
            }
        });
    }

    private String formatTime(Long value) {
        if (value < 10) {
            return (String.format("0%d", value));
        } else {
            return (String.valueOf(value));
        }
    }

}