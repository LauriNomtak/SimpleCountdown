package com.example.simplecountdown;

import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.Fade;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimerActivity2 extends AppCompatActivity {

    public static String SELECTED_MINUTES = "selected_minutes";
    public static String ALARM = "alarm";
    public static String NOTIFY = "notify";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer2);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //**** Alarm media player ****//
        int alarmSound = R.raw.crystal_bowl_bells;
        final MediaPlayer alarmMedia = MediaPlayer.create(this, alarmSound);

        //**** Notify media player ****//
        int notifySound = R.raw.bell_one_shot_high;
        final MediaPlayer notifyMedia = MediaPlayer.create(this, notifySound);

        Fade fade = new Fade();
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        //**** Init variables ****//
        String stringMinutes = "0";
        boolean alarmToggle = false;
        boolean notifyToggle = false;

        //**** Getting elements ****//
        final FrameLayout backgroundLayout = findViewById(R.id.backgroundLayout);
        final FloatingActionButton clockButton = findViewById(R.id.clockButton);
        final TextView ctMinutes = findViewById(R.id.ctMinutes);
        final TextView ctSeconds = findViewById(R.id.ct_seconds);
        final FrameLayout progressLayout = findViewById(R.id.progressLayout);
        final FrameLayout remainingLayout = findViewById(R.id.remainingLayout);

        //**** Getting values from previous activity ****//
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            stringMinutes = extras.getString(SELECTED_MINUTES);
            alarmToggle = Boolean.parseBoolean(extras.getString(ALARM));
            notifyToggle = Boolean.parseBoolean(extras.getString(NOTIFY));
        }

        boolean finalAlarmToggle = alarmToggle;
        boolean finalNotifyToggle = notifyToggle;
        ctMinutes.setText(formatTime(Long.valueOf(stringMinutes)));
        Long minutesInMills = Long.parseLong(stringMinutes) * 60 * 1000;

        //**** Countdown ****//
        CountDownTimer ct = new CountDownTimer(minutesInMills, 10) {

            float lastTimerHeight = 0;
            float timerHeight = 0;
            float lastRemainingHeight = 0;
            float remainingHeight = 0;

            public void onTick(long millisUntilFinished) {

                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) % 60;
                long millsFromStart = minutesInMills - millisUntilFinished;

                ctMinutes.setText(formatTime(minutes));
                ctSeconds.setText(formatTime(seconds));

                long displayMaxHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
                lastTimerHeight = timerHeight;
                timerHeight = (displayMaxHeight*millsFromStart)/minutesInMills;

                remainingHeight = (displayMaxHeight*60000*minutes)/minutesInMills;

                progressLayout.setMinimumHeight((int) (timerHeight));
                remainingLayout.setMinimumHeight((int) (remainingHeight));

                // Notify
                if (finalNotifyToggle) {
                    if (seconds == 0) {
                        notifyMedia.start();
                    }
                }
            }

            public void onFinish() {
                ctMinutes.setText("ST");
                ctSeconds.setText("OP");
                this.cancel();

                // Alarm
                if (finalAlarmToggle) {
                    alarmMedia.start();
                }
            }
        }.start();

        //**** Main button ****//
        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ct.cancel();
                alarmMedia.stop();

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

    //**** Functions ****//
    private String formatTime(Long value) {
        if (value < 10) {
            return (String.format("0%d", value));
        } else {
            return (String.valueOf(value));
        }
    }

}