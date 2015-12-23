package com.aziflaj.braintrainer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    CountDownTimer mTimer;
    TextView mTimerTextView;
    TextView mScoreTextView;
    TextView mQuestionTextView;
    TextView mMessageTextView;
    Button mRestartButton;
    ArrayList<Button> answerButtons;

    boolean timerRunning = false;
    int questions = 0;
    int answered = 0;
    int number1;
    int number2;
    String scoreFormat = "%d/%d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerTextView = (TextView) findViewById(R.id.timer_text_view);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mScoreTextView = (TextView) findViewById(R.id.score_text_view);
        mMessageTextView = (TextView) findViewById(R.id.message_test_view);
        mRestartButton = (Button) findViewById(R.id.restart_btn);

        answerButtons = new ArrayList<>();
        answerButtons.add((Button) findViewById(R.id.button1));
        answerButtons.add((Button) findViewById(R.id.button2));
        answerButtons.add((Button) findViewById(R.id.button3));
        answerButtons.add((Button) findViewById(R.id.button4));

        mMessageTextView.setVisibility(View.INVISIBLE);
        mRestartButton.setVisibility(View.INVISIBLE);

        nextQuestion();
        questions++;

        mTimer = new CountDownTimer(30000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // update the timer
                int secondsLeft = (int) (millisUntilFinished / 1000);
                mTimerTextView.setText(String.format("%02ds", secondsLeft));
            }

            @Override
            public void onFinish() {
                mTimerTextView.setText("30s");
                mMessageTextView.setText(String.format("Your score is " + scoreFormat, answered, questions));
                mMessageTextView.setVisibility(View.VISIBLE);
                mRestartButton.setVisibility(View.VISIBLE);
                timerRunning = false;
                for (Button btn : answerButtons) {
                    btn.setClickable(false);
                }
            }
        };
    }

    public void score(View view) {
        if (!timerRunning) {
            mTimer.start();
            timerRunning = true;
        }

        if (timerRunning) {
            evaluateAnswer(view.getTag().toString());
            nextQuestion();
            questions++;
        }
    }

    public void restart(View view) {
        for (Button btn : answerButtons) {
            btn.setClickable(true);
        }

        mMessageTextView.setVisibility(View.INVISIBLE);
        mRestartButton.setVisibility(View.INVISIBLE);
        answered = 0;
        nextQuestion();
        mScoreTextView.setText(String.format(scoreFormat, answered, questions));
        questions = 1;
    }

    private void nextQuestion() {
        number1 = (int) (Math.random() * 21);
        number2 = (int) (Math.random() * 21);

        mQuestionTextView.setText(String.format("%d + %d", number1, number2));

        // get a random button to assign the right answer
        Random random = new Random();
        Button randomButton = answerButtons.get(random.nextInt(answerButtons.size()));
        randomButton.setText(Integer.toString(number1 + number2));
        randomButton.setTag(Integer.toString(number1 + number2));

        // assign other values
        for (Button btn : answerButtons) {
            if (btn != randomButton) {
                int wrong = random.nextInt(50);
                btn.setTag(Integer.toString(wrong));
                btn.setTag(Integer.toString(wrong));
            }
        }

        mScoreTextView.setText(String.format(scoreFormat, answered, questions));
    }

    private void evaluateAnswer(String tag) {
        int result = Integer.parseInt(tag);
        if (number1 + number2 == result) {
            answered++;
            mScoreTextView.setText(String.format(scoreFormat, answered, questions));
        }
    }
}
