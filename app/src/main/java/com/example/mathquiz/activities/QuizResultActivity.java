package com.example.mathquiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.example.mathquiz.MainActivity;
import com.example.mathquiz.R;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.Random;

public class QuizResultActivity extends AppCompatActivity implements IUnityAdsInitializationListener {

    TextView theScoreTxt, bestScoreTxt, gameDifficultyTxt;
    int gameScore, bestScoreInt;
    public SharedPreferences prefs;
    private AdView adView;
    private InterstitialAd interstitialAd;
    private ProgressDialog pDialog;
    private String GameDifficulty;

    ImageView ResultImg, cupImg;
    TextView rightAnswersText, bestScoreText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        LoadAds();
        setBannerAds();

        theScoreTxt = (TextView) findViewById(R.id.txt_score);
        bestScoreTxt = (TextView) findViewById(R.id.txt_best_score);
        gameDifficultyTxt = (TextView) findViewById(R.id.game_difficultyTxt);

        // static
        rightAnswersText = (TextView)findViewById(R.id.right_answers_text);
        bestScoreText = (TextView) findViewById(R.id.best_score_text);
        ResultImg = (ImageView) findViewById(R.id.resultImg);
        cupImg = (ImageView) findViewById(R.id.CupImg);

        ResultImg.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.results_icon, 100, 100));

        cupImg.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.best_score_icon, 100, 100));

        Bundle resultBundle = getIntent().getExtras();
        gameScore = resultBundle.getInt("theResult");
        GameDifficulty = resultBundle.getString("Difficulty");

        theScoreTxt.setText("" + gameScore);
        gameDifficultyTxt.setText("Game Difficulty: " + GameDifficulty);

        prefs = getSharedPreferences("prefBestScore", Context.MODE_PRIVATE);
        bestScoreInt = prefs.getInt(GameDifficulty , 0);

        if (gameScore > bestScoreInt)
        {
            newRecord(gameScore);
            bestScoreTxt.setText("" + gameScore);
        }
        else {
            bestScoreTxt.setText("" + bestScoreInt);
        }


        // achievement
        setAch();

        Animation txtAnim = AnimationUtils.loadAnimation(this, R.anim.in_right);
        rightAnswersText.startAnimation(txtAnim);
        bestScoreText.startAnimation(txtAnim);

        Animation scoreAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        theScoreTxt.startAnimation(scoreAnim);
        bestScoreTxt.startAnimation(scoreAnim);

        Animation imgAnim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        ResultImg.startAnimation(imgAnim);

        //Unity Ads
        // Initialize the SDK:
        UnityAds.initialize(getApplicationContext(), getString(R.string.unityGameID), false, this);

    }


    private void newRecord(int newRecord)
    {
        if (GameDifficulty.contains("easy"))
        {
            prefs = getSharedPreferences("prefBestScore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("easy", newRecord);
            editor.apply();
        }
        if (GameDifficulty.contains("medium"))
        {
            prefs = getSharedPreferences("prefBestScore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("medium", newRecord);
            editor.apply();
        }
        if (GameDifficulty.contains("hard"))
        {
            prefs = getSharedPreferences("prefBestScore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("hard", newRecord);
            editor.apply();
        }
        if (GameDifficulty.contains("random"))
        {
            prefs = getSharedPreferences("prefBestScore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("random", newRecord);
            editor.apply();
        }

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
        showNetworkAds();
    }

    public void PlayAgainClick(View view)
    {
        Bundle difficulty = new Bundle();
        difficulty.putString("difficulty", GameDifficulty);
        Intent playAgain = new Intent(QuizResultActivity.this, StartGameActivity.class);
        playAgain.putExtras(difficulty);
        startActivity(playAgain);
        finish();
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight)
    {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight)
    {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void BackClick(View view)
    {
        showNetworkAds();
    }

    public void LoadAds()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getString(R.string.admob_interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        QuizResultActivity.this.interstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                        //  Toast.makeText(StartGameActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        QuizResultActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        Intent back = new Intent(QuizResultActivity.this, MainActivity.class);
                                        startActivity(back);
                                        finish();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        QuizResultActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");

                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        interstitialAd = null;
                    }
                });
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            Intent back = new Intent(QuizResultActivity.this, MainActivity.class);
            startActivity(back);
            finish();
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy()
    {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    // Unity Ads
    @Override
    public void onInitializationComplete() {
        //DisplayRewardedAd();
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.e("UnityAdsExample", "Unity Ads initialization failed with error: [" + error + "] " + message);

    }
    // Implement a function to load a rewarded ad. The ad will start to show once the ad has been loaded.
    public void DisplayRewardedAd () {
        UnityAds.load(getString(R.string.adUnitId), loadListener);
    }
    // Unity ads
    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show(QuizResultActivity.this, getString(R.string.adUnitId), new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private final IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);

        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
            if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
                // Reward the user for watching the ad to completion
            }
        }
    };

    private void showNetworkAds()
    {
        if (getString(R.string.admob_ads_work).contains("On") && getString(R.string.unity_ads_work).contains("On"))
        {
            int min = 1;
            int max = 2;
            int random = new Random().nextInt((max - min) + 1) + min;

            if(random == 1)
            {
                showInterstitial(); // show admob
            }
            else{
                DisplayRewardedAd(); // show unity
            }
        }
        else if (getString(R.string.admob_ads_work).contains("On"))
        {
            showInterstitial(); // show admob
        }
        else if (getString(R.string.unity_ads_work).contains("On"))
        {
            DisplayRewardedAd(); // show unity
        }
        else {
            showInterstitial(); // show admob
        }
    }


    private void setAch()
    {
        prefs = getSharedPreferences("achievement", Context.MODE_PRIVATE);
        if (bestScoreInt >= 5)
        {
            prefs.edit().putInt("Score_5_Ach", 1).apply();
        }
        if (bestScoreInt >= 10)
        {
            prefs.edit().putInt("Score_10_Ach", 1).apply();
        }
        if (bestScoreInt >= 50)
        {
            prefs.edit().putInt("Score_50_Ach", 1).apply();
        }
    }

}