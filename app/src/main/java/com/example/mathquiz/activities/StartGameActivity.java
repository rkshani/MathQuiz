package com.example.mathquiz.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.example.mathquiz.R;
import com.example.mathquiz.helper.Questions;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StartGameActivity extends AppCompatActivity {

    private TextView textViewTime, questionText, numberOfQuestion;
    private Button btn_AnswerOne, btn_AnswerTwo, btn_AnswerThree, btn_AnswerFour;

    private AdView adView;

    private Questions questions;

    private ProgressBar progressBar;
    private CountDownTimer myTimer;

    private int QuestionRandomId, RightCount, WrongCount;
    private int QuestionNumber = 1;
    public SharedPreferences prefs;
    private String theCorrectAnswer;
    private MediaPlayer quizTimerSound;
    final long timeCountInMilliSeconds = 30000;
    private String DifficultyGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);


        Bundle DifficultyBundle = getIntent().getExtras();
        DifficultyGame = DifficultyBundle.getString("difficulty");

        numberOfQuestion = (TextView) findViewById(R.id.NumberOf_Question);
        questionText = (TextView) findViewById(R.id.Question_text);
        btn_AnswerOne = (Button) findViewById(R.id.btn_Answer_one);
        btn_AnswerTwo = (Button) findViewById(R.id.btn_Answer_two);
        btn_AnswerThree = (Button) findViewById(R.id.btn_Answer_three);
        btn_AnswerFour = (Button) findViewById(R.id.btn_Answer_four);
        progressBar = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        setBannerAds();
        getNextQuestion();
        setProgressTimer();

        questionText.setShadowLayer(5, 1, 1, Color.BLACK);

        btn_AnswerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_AnswerOne.getText().toString() == theCorrectAnswer)
                {
                    myTimer.cancel();
                    setQuizStatistic("rightQuizStat", RightCount);
                    delayNextQuestion();
                    CorrectAnswer(btn_AnswerOne);
                }
                else
                {
                    myTimer.cancel();
                    setQuizStatistic("wrongQuizStat", WrongCount);
                    delayExit();
                    WrongAnswer(btn_AnswerOne);
                }
            }
        });

        btn_AnswerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_AnswerTwo.getText().toString() == theCorrectAnswer)
                {
                    myTimer.cancel();
                    setQuizStatistic("rightQuizStat", RightCount);
                    delayNextQuestion();
                    CorrectAnswer(btn_AnswerTwo);
                }
                else
                {
                    myTimer.cancel();
                    setQuizStatistic("wrongQuizStat", WrongCount);
                    delayExit();
                    WrongAnswer(btn_AnswerTwo);
                }
            }
        });

        btn_AnswerThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_AnswerThree.getText().toString() == theCorrectAnswer)
                {
                    myTimer.cancel();
                    setQuizStatistic("rightQuizStat", RightCount);
                    delayNextQuestion();
                    CorrectAnswer(btn_AnswerThree);
                }
                else
                {
                    myTimer.cancel();
                    setQuizStatistic("wrongQuizStat", WrongCount);
                    delayExit();
                    WrongAnswer(btn_AnswerThree);
                }
            }
        });

        btn_AnswerFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_AnswerFour.getText().toString() == theCorrectAnswer)
                {
                    myTimer.cancel();
                    setQuizStatistic("rightQuizStat", RightCount);
                    delayNextQuestion();
                    CorrectAnswer(btn_AnswerFour);
                }
                else
                {
                    myTimer.cancel();
                    setQuizStatistic("wrongQuizStat", WrongCount);
                    delayExit();
                    WrongAnswer(btn_AnswerFour);
                }
            }
        });


    }

    private void setProgressTimer()
    {
        myTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                progressBar.setProgress(Integer.parseInt(String.valueOf(time)));
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                if (Integer.parseInt(String.valueOf(time)) <= 15)
                {
                    textViewTime.setShadowLayer(5, 1, 1, Color.BLACK);
                    textViewTime.setTextColor(getResources().getColor(R.color.red_A400));
                }
                else {
                    textViewTime.setShadowLayer(1, 1, 1, Color.BLACK);
                    textViewTime.setTextColor(getResources().getColor(R.color.yellow_600));
                }
            }
            public void onFinish() {
                Bundle bundle = new Bundle();
                bundle.putInt("theResult", QuestionNumber - 1);
                bundle.putString("Difficulty", DifficultyGame);
                Intent outIntent = new Intent(StartGameActivity.this, QuizResultActivity.class);
                outIntent.putExtras(bundle);
                startActivity(outIntent);
                myTimer.cancel();
                quizTimerSound.stop();
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                finish();

            }
        }.start();
    }

    private String hmsTimeFormatter(long milliSeconds) {

        @SuppressLint("DefaultLocale") String hms = String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }

    private void delayExit()
    {
        quizTimerSound.stop();
        disableClickButton();

        if (btn_AnswerOne.getText().toString() == theCorrectAnswer)
        {
            CorrectAnswer(btn_AnswerOne);
        }
        if (btn_AnswerTwo.getText().toString() == theCorrectAnswer)
        {
            CorrectAnswer(btn_AnswerTwo);
        }
        if (btn_AnswerThree.getText().toString() == theCorrectAnswer)
        {
            CorrectAnswer(btn_AnswerThree);
        }
        if (btn_AnswerFour.getText().toString() == theCorrectAnswer)
        {
            CorrectAnswer(btn_AnswerFour);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putInt("theResult", QuestionNumber - 1);
                bundle.putString("Difficulty", DifficultyGame);
                Intent outIntent = new Intent(StartGameActivity.this, QuizResultActivity.class);
                outIntent.putExtras(bundle);
                startActivity(outIntent);
                finish();
            }
        }, 2000);
    }

    private void delayNextQuestion()
    {
        quizTimerSound.stop();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                QuestionNumber++;
                getNextQuestion();
                enableClickButton();
                setProgressTimer();
            }
        }, 2000);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void getNextQuestion()
    {
        quizTimerSound = MediaPlayer.create(this, R.raw.timer_sound);

        numberOfQuestion.setText("" + getString(R.string.Question) + " " + QuestionNumber);
        setQuestionAnimation();
        setOptionsAnimation();


        getGameDifficulty();
        quizTimerSound.start();


        btn_AnswerOne.setBackground(getDrawable(R.drawable.button));
        btn_AnswerTwo.setBackground(getDrawable(R.drawable.button));
        btn_AnswerThree.setBackground(getDrawable(R.drawable.button));
        btn_AnswerFour.setBackground(getDrawable(R.drawable.button));
    }

    private void getGameDifficulty()
    {
        if (DifficultyGame.toString().contains("easy"))
        {
            QuestionRandomId = randomNumber(1,50);
            String[] theQuestion = questions.getQuestionAndOptionsEasy(QuestionRandomId);
            questionText.setText(theQuestion[0]);
            btn_AnswerOne.setText(theQuestion[1]);
            btn_AnswerTwo.setText(theQuestion[2]);
            btn_AnswerThree.setText(theQuestion[3]);
            btn_AnswerFour.setText(theQuestion[4]);
            theCorrectAnswer = theQuestion[5];
        }
        if (DifficultyGame.contains("medium"))
        {
            QuestionRandomId = randomNumber(1,50);
            String[] theQuestion = questions.getQuestionAndOptionsMedium(QuestionRandomId);
            questionText.setText(theQuestion[0]);
            btn_AnswerOne.setText(theQuestion[1]);
            btn_AnswerTwo.setText(theQuestion[2]);
            btn_AnswerThree.setText(theQuestion[3]);
            btn_AnswerFour.setText(theQuestion[4]);
            theCorrectAnswer = theQuestion[5];
        }
        if (DifficultyGame.contains("hard"))
        {
            QuestionRandomId = randomNumber(1,50);
            String[] theQuestion = questions.getQuestionAndOptionsHard(QuestionRandomId);
            questionText.setText(theQuestion[0]);
            btn_AnswerOne.setText(theQuestion[1]);
            btn_AnswerTwo.setText(theQuestion[2]);
            btn_AnswerThree.setText(theQuestion[3]);
            btn_AnswerFour.setText(theQuestion[4]);
            theCorrectAnswer = theQuestion[5];
        }
        if (DifficultyGame.contains("random"))
        {
            QuestionRandomId = randomNumber(1,100);
            String[] theQuestion = questions.getQuestionAndOptions(QuestionRandomId);
            questionText.setText(theQuestion[0]);
            btn_AnswerOne.setText(theQuestion[1]);
            btn_AnswerTwo.setText(theQuestion[2]);
            btn_AnswerThree.setText(theQuestion[3]);
            btn_AnswerFour.setText(theQuestion[4]);
            theCorrectAnswer = theQuestion[5];
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void CorrectAnswer(Button button)
    {
        button.setBackground(getDrawable(R.drawable.btn_correct_answer));

        // achievement
        int levelTimer = Integer.parseInt(textViewTime.getText().toString());
        if (levelTimer >= 25)
        {
            prefs = getSharedPreferences("achievement", Context.MODE_PRIVATE);
            prefs.edit().putInt("TimerAch", 1).apply();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void WrongAnswer(Button button)
    {
        button.setBackground(getDrawable(R.drawable.btn_wrong_answer));
    }

    private void enableClickButton()
    {
        btn_AnswerOne.setClickable(true);
        btn_AnswerTwo.setClickable(true);
        btn_AnswerThree.setClickable(true);
        btn_AnswerFour.setClickable(true);
    }

    private void disableClickButton()
    {
        btn_AnswerOne.setClickable(false);
        btn_AnswerTwo.setClickable(false);
        btn_AnswerThree.setClickable(false);
        btn_AnswerFour.setClickable(false);
    }

    private void setProgressValue(final int progress)
    {
        progressBar.setProgress(progress);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                StartGameActivity.this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (progressBar.getProgress() < progressBar.getMax())
                        {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            setProgressValue(progress + 1);
                        }
                        else {
                            Looper.myLooper().quit();
                            Toast.makeText(getApplicationContext(), getString(R.string.TimeOut), Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });
        thread.start();
    }

    private int randomNumber(int min, int max)
    {
        Random random = new Random();
        int rr = random.nextInt(max - min) + min;

        return rr;
    }

    private void setQuizStatistic(String key, int count)
    {
        prefs = getSharedPreferences("prefName", Context.MODE_PRIVATE);
        count = prefs.getInt(key , 0);

        count = count + 1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, count);
        editor.commit();
    }

    private void setQuestionAnimation()
    {
        Animation queAnim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        questionText.startAnimation(queAnim);
    }

    private void setOptionsAnimation()
    {
        Animation queAnim = AnimationUtils.loadAnimation(this, R.anim.in_right);
        btn_AnswerOne.startAnimation(queAnim);
        btn_AnswerTwo.startAnimation(queAnim);
        Animation queAnimfast = AnimationUtils.loadAnimation(this, R.anim.in_right);
        btn_AnswerThree.startAnimation(queAnimfast);
        btn_AnswerFour.startAnimation(queAnimfast);
    }


    private void setBannerAds()
    {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myTimer.cancel();
        quizTimerSound.stop();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        quizTimerSound.stop();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
        quizTimerSound.stop();
    }

}