package com.example.mathquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.BuildConfig;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.example.mathquiz.activities.AchievementsActivity;
import com.example.mathquiz.activities.StartGameActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView appLogo;
    private Button startBtn, btnAchievements, shareBtn, rateBtn, aboutBtn;
    private static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appLogo = findViewById(R.id.appLogoImg);
        btnAchievements = findViewById(R.id.btn_Achievements);
        startBtn = findViewById(R.id.StartQuiz);
        shareBtn = findViewById(R.id.ShareGameQuiz);
        rateBtn = findViewById(R.id.RateGameQuiz);
        aboutBtn = findViewById(R.id.AboutQuiz);

        setAnimation();
        setBannerAds();

        PACKAGE_NAME = getApplicationContext().getPackageName();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogGameDifficulty();
            }
        });

        btnAchievements.setOnClickListener(v->
        {
            Intent startActivity = new Intent(MainActivity.this, AchievementsActivity.class);
            startActivity(startActivity);
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String text = getString(R.string.ShareText);
                text = text + "\n http://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
                i.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(i, "choose one"));
            }
        });

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRate();
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAbout();
            }
        });

    }

    private void setAnimation()
    {
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.anim);
        appLogo.startAnimation(myanim);
        btnAchievements.startAnimation(myanim);
        startBtn.startAnimation(myanim);
        shareBtn.startAnimation(myanim);
        rateBtn.startAnimation(myanim);
        aboutBtn.startAnimation(myanim);
    }

    private void setRate()
    {
        try {
            Uri uri = Uri.parse("market://details?id=" + PACKAGE_NAME);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + PACKAGE_NAME)));
        }
    }

    @SuppressLint("SetTextI18n")
    private void showDialogAbout()
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about_game);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText(getString(R.string.About_Version)+ " " + BuildConfig.VERSION_NAME);

        ((View) dialog.findViewById(R.id.bt_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_privacy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // You Privacy Policy Url
                Uri uri = Uri.parse("https://sites.google.com/view/writing-speed-quiz/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ((Button) dialog.findViewById(R.id.bt_rate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setRate();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showDialogGameDifficulty()
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_game_difficulty);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        ((View) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_random)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle difficulty = new Bundle();
                difficulty.putString("difficulty", "random");
                Intent startActivity = new Intent(MainActivity.this, StartGameActivity.class);
                startActivity.putExtras(difficulty);
                startActivity(startActivity);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                dialog.dismiss();
            }
        });


        ((Button) dialog.findViewById(R.id.bt_easy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle difficulty = new Bundle();
                difficulty.putString("difficulty", "easy");
                Intent startActivity = new Intent(MainActivity.this, StartGameActivity.class);
                startActivity.putExtras(difficulty);
                startActivity(startActivity);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_medium)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle difficulty = new Bundle();
                difficulty.putString("difficulty", "medium");
                Intent startActivity = new Intent(MainActivity.this, StartGameActivity.class);
                startActivity.putExtras(difficulty);
                startActivity(startActivity);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_hard)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle difficulty = new Bundle();
                difficulty.putString("difficulty", "hard");
                Intent startActivity = new Intent(MainActivity.this, StartGameActivity.class);
                startActivity.putExtras(difficulty);
                startActivity(startActivity);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void setBannerAds()
    {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}