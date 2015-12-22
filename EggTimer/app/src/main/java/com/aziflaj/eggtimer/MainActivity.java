package com.aziflaj.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean timerRunning = false;
    int seconds;
    SeekBar mSeekBar;
    Button timerBtn;
    TextView countDownTextView;
    CountDownTimer mCountDownTimer;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeekBar = (SeekBar) findViewById(R.id.timer_seekbar);
        timerBtn = (Button) findViewById(R.id.start_stop_button);
        countDownTextView = (TextView) findViewById(R.id.time_text_view);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seconds = progress * 6;
                int minutesLeft = seconds / 60;
                int nextSeconds = seconds % 60;
                countDownTextView.setText(String.format("%d:%02d", minutesLeft, nextSeconds));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void toggleTimer(View view) {
        if (timerRunning) {
            // stop the timer
            mCountDownTimer.cancel();

            timerBtn.setText("Start");
            mSeekBar.setEnabled(true);
            timerRunning = false;
            seconds = 0;
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(seconds * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutesLeft = (int) ((millisUntilFinished / 1000) / 60);
                int nextSeconds = (int) ((millisUntilFinished / 1000) % 60);
                countDownTextView.setText(String.format("%d:%02d", minutesLeft, nextSeconds));
            }

            @Override
            public void onFinish() {
                timerBtn.setText("Start");
                mSeekBar.setEnabled(true);
                seconds = 0;
                countDownTextView.setText("0:00");
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.horn);
                mediaPlayer.start();
            }
        };

        mCountDownTimer.start();
        timerBtn.setText("Stop");
        mSeekBar.setEnabled(false);
        timerRunning = true;
    }
}
