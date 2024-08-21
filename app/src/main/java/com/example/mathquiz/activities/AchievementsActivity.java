package com.example.mathquiz.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathquiz.R;

public class AchievementsActivity extends AppCompatActivity {

    CardView cv_timer_achievement, cv_score_5_achievement, cv_score_10_achievement, cv_score_50_achievement;
    SharedPreferences prefs;
    int TimerAch, Score_5_Ach, Score_10_Ach, Score_50_Ach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        setToolbar();
        iniViews();

        prefs = getSharedPreferences("achievement", Context.MODE_PRIVATE);
        TimerAch = prefs.getInt("TimerAch", 0);
        Score_5_Ach = prefs.getInt("Score_5_Ach", 0);
        Score_10_Ach = prefs.getInt("Score_10_Ach", 0);
        Score_50_Ach = prefs.getInt("Score_50_Ach", 0);

        if (TimerAch == 1)
        {
            cv_timer_achievement.setAlpha(1);
        }
        if (Score_5_Ach == 1)
        {
            cv_score_5_achievement.setAlpha(1);
        }
        if (Score_10_Ach == 1)
        {
            cv_score_10_achievement.setAlpha(1);
        }
        if (Score_50_Ach == 1)
        {
            cv_score_50_achievement.setAlpha(1);
        }

        cv_timer_achievement.setOnClickListener(v->
        {
            if (TimerAch == 0)
            {
                Toast.makeText(this, getString(R.string.not_completed), Toast.LENGTH_SHORT).show();
            }
        });

        cv_score_5_achievement.setOnClickListener(v->
        {
            if (Score_5_Ach == 0)
            {
                Toast.makeText(this, getString(R.string.not_completed), Toast.LENGTH_SHORT).show();
            }
        });

        cv_score_10_achievement.setOnClickListener(v->
        {
            if (Score_10_Ach == 0)
            {
                Toast.makeText(this, getString(R.string.not_completed), Toast.LENGTH_SHORT).show();
            }
        });

        cv_score_50_achievement.setOnClickListener(v->
        {
            if (Score_50_Ach == 0)
            {
                Toast.makeText(this, getString(R.string.not_completed), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.toolbar_achievements);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void iniViews(){
        cv_timer_achievement = findViewById(R.id.cv_timer_achievement);
        cv_score_5_achievement = findViewById(R.id.cv_score_5_achievement);
        cv_score_10_achievement = findViewById(R.id.cv_score_10_achievement);
        cv_score_50_achievement = findViewById(R.id.cv_score_50_achievement);
    }
}