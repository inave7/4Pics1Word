package com.belaku.a4p1w;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

//App ID - ca-app-pub-7503134305147968~4153188574
public class MainActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    private String TAG = "MAct7";
    private ArrayList<String> imageUrls = new ArrayList<>(), Options = new ArrayList<>(), Answers = new ArrayList<>();

    private ImageView imageViewPicture, imageViewCoins;
    private TextView TxScore, TxAns, TxAd, TxAns1, TxAns2, TxAns3, TxAns4, TxAns5, TxAns6, TxAns7, TxAns8, TxAns9,
            TxOpt1, TxOpt2, TxOpt3, TxOpt4, TxOpt5, TxOpt6,
            TxOpt7, TxOpt8, TxOpt9, TxOpt10, TxOpt11, TxOpt12;
    private Button BtnNext, BtnSubmit, BtnCoins;
    private int oldCount, count;
    private RewardedAd rewardedAd;
    private boolean removed = false;

    Animation animBounce, animBlink, animSlide;
    public static int Score;
    public static final String MyPrefs = "MyPrefs", MyScorePrefs = "MyScorePrefs";
    private SharedPreferences.Editor editorScore;
    private SharedPreferences sharedPreferences, sharedPreferencesScore;

    DataSnapshot dataSnapshot;
    int totalQs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        findViewByIds();

        SharedPreferences sharedPreferencesScore = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        Score = sharedPreferencesScore.getInt("your_int_key", 100);


        editorScore = sharedPreferencesScore.edit();


        TxScore.setText("" + Score); // getting Integer
        if (Score == 0) {
            makeToast("Not eligible to continue the Game");
            imageViewCoins.setVisibility(View.INVISIBLE);
            DisableViews();
        }

        isFirstTime();
    //    makeToast("SCORE - " + Score);

        // load the animation
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide);

        animBounce.setAnimationListener(this);
        animBlink.setAnimationListener(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Data");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "TOTALQUESTIONS - " + String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                totalQs = (int) dataSnapshot.getChildrenCount();
                for (int i = 1; i <= dataSnapshot.getChildrenCount(); i++) {
                    Toast.makeText(getApplicationContext(), i + "). " + dataSnapshot.child("01").child("image").getValue().toString(), Toast.LENGTH_SHORT).show();
                    String index = "0" + i;
                    imageUrls.add(dataSnapshot.child(index).child("image").getValue().toString());
                    Options.add(dataSnapshot.child(index).child("Options").getValue().toString());
                    Answers.add(dataSnapshot.child(index).child("answer").getValue().toString());


                }

                /*makeToast("QPs - " + imageUrls.size() + "\n" +
                        "As - " + Answers.size() + "\n" +
                        "Os - " + Options.size());*/

                loadQ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void isFirstTime() {
        SharedPreferences sp = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.commit();
            makeToast("FIRSTTIME");
            Score = 100;
            editorScore.putInt("your_int_key", Score);
            editorScore.commit();
        }
    }

    private void loadQ() {
        //   makeToast("Count - " + count);


        while (true) {
            oldCount = new Random().nextInt(totalQs);
            if (oldCount != count) {
                count = oldCount;
                break;
            } else {
                oldCount = new Random().nextInt(totalQs);
                if (count != oldCount)
                    count = oldCount;
                break;
            }
        }


        Picasso.with(MainActivity.this)
                .load(imageUrls.get(count))
                .into(imageViewPicture);


        for (int y = 0; y < Answers.get(count).length(); y++) {
            switch (y) {
                case 0:
                    TxAns1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    TxAns2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    TxAns3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    TxAns4.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    TxAns5.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    TxAns6.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    TxAns7.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    TxAns8.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    TxAns9.setVisibility(View.VISIBLE);
                    break;


            }
        }

        for (int z = 0; z < Options.get(count).length(); z++) {
            switch (z) {
                case 0:
                    TxOpt1.setText("" + Options.get(count).charAt(z));
                    break;
                case 1:
                    TxOpt2.setText("" + Options.get(count).charAt(z));
                    break;
                case 2:
                    TxOpt3.setText("" + Options.get(count).charAt(z));
                    break;
                case 3:
                    TxOpt4.setText("" + Options.get(count).charAt(z));
                    break;
                case 4:
                    TxOpt5.setText("" + Options.get(count).charAt(z));
                    break;
                case 5:
                    TxOpt6.setText("" + Options.get(count).charAt(z));
                    break;
                case 6:
                    TxOpt7.setText("" + Options.get(count).charAt(z));
                    break;
                case 7:
                    TxOpt8.setText("" + Options.get(count).charAt(z));
                    break;
                case 8:
                    TxOpt9.setText("" + Options.get(count).charAt(z));
                    break;
                case 9:
                    TxOpt10.setText("" + Options.get(count).charAt(z));
                    break;
                case 10:
                    TxOpt11.setText("" + Options.get(count).charAt(z));
                    break;
                case 11:
                    TxOpt12.setText("" + Options.get(count).charAt(z));
                    break;

            }

        }

        Animate(imageViewPicture);
    }

    private void Animate(final ImageView imageViewPicture) {
        // Prepare the View for the animation
        imageViewPicture.startAnimation(animBounce);

        TxOpt1.startAnimation(animBlink);
        TxOpt2.startAnimation(animBlink);
        TxOpt3.startAnimation(animBlink);
        TxOpt4.startAnimation(animBlink);
        TxOpt5.startAnimation(animBlink);
        TxOpt6.startAnimation(animBlink);
        TxOpt7.startAnimation(animBlink);
        TxOpt8.startAnimation(animBlink);
        TxOpt9.startAnimation(animBlink);
        TxOpt10.startAnimation(animBlink);
        TxOpt11.startAnimation(animBlink);
        TxOpt12.startAnimation(animBlink);


    }

    private void findViewByIds() {

        TxScore = findViewById(R.id.tx_score);
        TxScore.bringToFront();

        imageViewPicture = findViewById(R.id.imgv);
        imageViewCoins = findViewById(R.id.imgv_coins);

        BtnSubmit = findViewById(R.id.btn_submit);
        BtnCoins = findViewById(R.id.btn_coins);

        BtnSubmit.setOnClickListener(MainActivity.this);
        BtnCoins.setOnClickListener(MainActivity.this);

        TxAd = findViewById(R.id.tx_del_letter);
        TxAns = findViewById(R.id.tx_del_letters);
        TxAd.setOnClickListener(MainActivity.this);
        TxAns.setOnClickListener(MainActivity.this);
        TxAns1 = findViewById(R.id.tx_ans1);
        TxAns2 = findViewById(R.id.tx_ans2);
        TxAns3 = findViewById(R.id.tx_ans3);
        TxAns4 = findViewById(R.id.tx_ans4);
        TxAns5 = findViewById(R.id.tx_ans5);
        TxAns6 = findViewById(R.id.tx_ans6);
        TxAns7 = findViewById(R.id.tx_ans7);
        TxAns8 = findViewById(R.id.tx_ans8);
        TxAns9 = findViewById(R.id.tx_ans9);

        TxAns1.setOnClickListener(MainActivity.this);
        TxAns2.setOnClickListener(MainActivity.this);
        TxAns3.setOnClickListener(MainActivity.this);
        TxAns4.setOnClickListener(MainActivity.this);
        TxAns5.setOnClickListener(MainActivity.this);
        TxAns6.setOnClickListener(MainActivity.this);
        TxAns7.setOnClickListener(MainActivity.this);
        TxAns8.setOnClickListener(MainActivity.this);
        TxAns9.setOnClickListener(MainActivity.this);


        TxOpt1 = findViewById(R.id.tx_opt1);
        TxOpt2 = findViewById(R.id.tx_opt2);
        TxOpt3 = findViewById(R.id.tx_opt3);
        TxOpt4 = findViewById(R.id.tx_opt4);
        TxOpt5 = findViewById(R.id.tx_opt5);
        TxOpt6 = findViewById(R.id.tx_opt6);
        TxOpt7 = findViewById(R.id.tx_opt7);
        TxOpt8 = findViewById(R.id.tx_opt8);
        TxOpt9 = findViewById(R.id.tx_opt9);
        TxOpt10 = findViewById(R.id.tx_opt10);
        TxOpt11 = findViewById(R.id.tx_opt11);
        TxOpt12 = findViewById(R.id.tx_opt12);

        TxOpt1.setOnClickListener(MainActivity.this);
        TxOpt2.setOnClickListener(MainActivity.this);
        TxOpt3.setOnClickListener(MainActivity.this);
        TxOpt4.setOnClickListener(MainActivity.this);
        TxOpt5.setOnClickListener(MainActivity.this);
        TxOpt6.setOnClickListener(MainActivity.this);
        TxOpt7.setOnClickListener(MainActivity.this);
        TxOpt8.setOnClickListener(MainActivity.this);
        TxOpt9.setOnClickListener(MainActivity.this);
        TxOpt10.setOnClickListener(MainActivity.this);
        TxOpt11.setOnClickListener(MainActivity.this);
        TxOpt12.setOnClickListener(MainActivity.this);
    }

    private void makeToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        Log.d(TAG, s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_opt1:
                updateAns(TxOpt1);
                break;
            case R.id.tx_opt2:
                updateAns(TxOpt2);
                break;
            case R.id.tx_opt3:
                updateAns(TxOpt3);
                break;
            case R.id.tx_opt4:
                updateAns(TxOpt4);
                break;
            case R.id.tx_opt5:
                updateAns(TxOpt5);
                break;
            case R.id.tx_opt6:
                updateAns(TxOpt6);
                break;
            case R.id.tx_opt7:
                updateAns(TxOpt7);
                break;
            case R.id.tx_opt8:
                updateAns(TxOpt8);
                break;
            case R.id.tx_opt9:
                updateAns(TxOpt9);
                break;
            case R.id.tx_opt10:
                updateAns(TxOpt10);
                break;
            case R.id.tx_opt11:
                updateAns(TxOpt11);
                break;
            case R.id.tx_opt12:
                updateAns(TxOpt12);
                break;


            case R.id.btn_submit:
                if (CheckAnswer()) {
                    makeToast("Correct answer");
                    Score = Score + 10;
                    editorScore.putInt("your_int_key", Score);
                    editorScore.commit();
                    TxScore.setText("" + Score);
                    clearAllAns();
                } else {
                    makeToast("Wrong answer");
                    Score = Score - 10;
                    editorScore.putInt("your_int_key", Score);
                    editorScore.commit();
                    if (Score == 0) {
                        makeToast("Not eligible to continue the Game");
                        imageViewCoins.setVisibility(View.INVISIBLE);
                        DisableViews();
                    }
                    TxScore.setText("" + Score);
                    clearAllAns();
                }

                loadQ();
                break;
            case R.id.btn_coins:
                CoinsDialog();
                break;
            case R.id.tx_del_letter:
                removed = false;
                makeToast("Clckd");
                loadAd();
                showAd();
                break;
            case R.id.tx_del_letters:
                removed = false;
                makeToast("Clckd");
                loadAd();
                showAd();
                removeLetters();
                break;

            case R.id.tx_ans1:
                TxAns1.setText("");
                break;
            case R.id.tx_ans2:
                TxAns2.setText("");
                break;
            case R.id.tx_ans3:
                TxAns3.setText("");
                break;
            case R.id.tx_ans4:
                TxAns4.setText("");
                break;
            case R.id.tx_ans5:
                TxAns5.setText("");
                break;
            case R.id.tx_ans6:
                TxAns6.setText("");
                break;
            case R.id.tx_ans7:
                TxAns7.setText("");
                break;
            case R.id.tx_ans8:
                TxAns8.setText("");
                break;
            case R.id.tx_ans9:
                TxAns9.setText("");
                break;


        }
    }

    private void CoinsDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coins_dialog);

        Button BtnPay;


        BtnPay = dialog.findViewById(R.id.coins_dialog_btn_pay);


        BtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                makeToast("Yet2Impl, Bank servers preparing.. :) ");
            }
        });

        dialog.show();
    }

    private void DisableViews() {
        imageViewPicture.setEnabled(false);
        TxOpt1.setEnabled(false);
        TxOpt2.setEnabled(false);
        TxOpt3.setEnabled(false);
        TxOpt4.setEnabled(false);
        TxOpt5.setEnabled(false);
        TxOpt6.setEnabled(false);
        TxOpt7.setEnabled(false);
        TxOpt8.setEnabled(false);
        TxOpt9.setEnabled(false);
        TxOpt10.setEnabled(false);
        TxOpt11.setEnabled(false);
        TxOpt12.setEnabled(false);

        TxOpt1.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt2.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt3.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt4.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt5.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt6.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt7.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt8.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt9.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt10.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt11.setTextColor(getResources().getColor(android.R.color.darker_gray));
        TxOpt12.setTextColor(getResources().getColor(android.R.color.darker_gray));

        BtnSubmit.setEnabled(false);
        BtnCoins.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

    }

    private void removeLetters() {

        removeLetter(TxOpt1);
        removeLetter(TxOpt2);
        removeLetter(TxOpt3);
        removeLetter(TxOpt4);
        removeLetter(TxOpt5);
        removeLetter(TxOpt6);
        removeLetter(TxOpt7);
        removeLetter(TxOpt8);
        removeLetter(TxOpt9);
        removeLetter(TxOpt10);
        removeLetter(TxOpt11);
        removeLetter(TxOpt12);


    }

    private void removeLetter(TextView txOpt) {
        if (!Answers.get(count).contains(txOpt.getText()))
            txOpt.setText("");
    }

    private void showAd() {
        if (this.rewardedAd.isLoaded()) {

            RewardedAdCallback rewardedAdCallback = new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    if (TxOpt1.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt1.getText())) {
                        TxOpt1.setText("");
                        removed = true;
                    } else if (TxOpt2.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt2.getText())) {
                        TxOpt2.setText("");
                        removed = true;
                    } else if (TxOpt3.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt3.getText())) {
                        TxOpt3.setText("");
                        removed = true;
                    } else if (TxOpt4.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt4.getText())) {
                        TxOpt4.setText("");
                        removed = true;
                    } else if (TxOpt5.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt5.getText())) {
                        TxOpt5.setText("");
                        removed = true;
                    } else if (TxOpt6.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt6.getText())) {
                        TxOpt6.setText("");
                        removed = true;
                    } else if (TxOpt7.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt7.getText())) {
                        TxOpt7.setText("");
                        removed = true;
                    } else if (TxOpt8.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt8.getText())) {
                        TxOpt8.setText("");
                        removed = true;
                    } else if (TxOpt9.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt9.getText())) {
                        TxOpt9.setText("");
                        removed = true;
                    } else if (TxOpt10.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt10.getText())) {
                        TxOpt10.setText("");
                        removed = true;
                    } else if (TxOpt11.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt11.getText())) {
                        TxOpt11.setText("");
                        removed = true;
                    } else if (TxOpt12.getText().length() != 0 && !removed && !Answers.get(count).contains(TxOpt12.getText())) {
                        TxOpt12.setText("");
                        removed = true;
                    }
                }
            };
            this.rewardedAd.show(this, rewardedAdCallback);
        }
    }

    private void loadAd() {

        rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                makeToast("Ad loaded successfully");
                showAd();
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                makeToast("Ad failed to load.");
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    private boolean CheckAnswer() {

        if (TxAns1.getText().length() == 0)
            return false;
        if (TxAns2.getText().length() == 0)
            return false;
        if (TxAns2.getText().length() == 0)
            return false;
        if (TxAns2.getText().length() == 0)
            return false;
        if (TxAns2.getText().length() == 0)
            return false;
        if (TxAns2.getText().length() == 0)
            return false;
        if (TxAns2.getText().length() == 0)
            return false;
        if (TxAns2.getText().length() == 0)
            return false;
        if (TxAns2.getText().length() == 0)
            return false;


        //  makeToast(Answers.get(count).charAt(0));
        if (TxAns1.getText().length() != 0)
            if (!TxAns1.getText().toString().equals(String.valueOf(Answers.get(count).charAt(0))))
                return false;
            else if (TxAns2.getText().length() != 0)
                if (!TxAns2.getText().toString().equals(String.valueOf(Answers.get(count).charAt(1))))
                    return false;
                else if (TxAns3.getText().length() != 0)
                    if (!TxAns3.getText().toString().equals(String.valueOf(Answers.get(count).charAt(2))))
                        return false;
                    else if (TxAns4.getText().length() != 0)
                        if (!TxAns4.getText().toString().equals(String.valueOf(Answers.get(count).charAt(3))))
                            return false;
                        else if (TxAns5.getText().length() != 0)
                            if (!TxAns5.getText().toString().equals(String.valueOf(Answers.get(count).charAt(4))))
                                return false;
                            else if (TxAns6.getText().length() != 0)
                                if (!TxAns6.getText().toString().equals(String.valueOf(Answers.get(count).charAt(5))))
                                    return false;
                                else if (TxAns7.getText().length() != 0)
                                    if (!TxAns7.getText().toString().equals(String.valueOf(Answers.get(count).charAt(6))))
                                        return false;
                                    else if (TxAns8.getText().length() != 0)
                                        if (!TxAns8.getText().toString().equals(String.valueOf(Answers.get(count).charAt(7))))
                                            return false;
                                        else if (TxAns9.getText().length() != 0)
                                            if (!TxAns9.getText().toString().equals(String.valueOf(Answers.get(count).charAt(8))))
                                                return false;


        return true;
    }

    private void updateAns(TextView txOpt) {
        if (TxAns1.getText().length() == 0)
            TxAns1.setText(txOpt.getText());
        else if (TxAns2.getText().length() == 0)
            TxAns2.setText(txOpt.getText());
        else if (TxAns3.getText().length() == 0)
            TxAns3.setText(txOpt.getText());
        else if (TxAns4.getText().length() == 0)
            TxAns4.setText(txOpt.getText());
        else if (TxAns5.getText().length() == 0)
            TxAns5.setText(txOpt.getText());
        else if (TxAns6.getText().length() == 0)
            TxAns6.setText(txOpt.getText());
        else if (TxAns7.getText().length() == 0)
            TxAns7.setText(txOpt.getText());
        else if (TxAns8.getText().length() == 0)
            TxAns8.setText(txOpt.getText());
        else if (TxAns9.getText().length() == 0)
            TxAns9.setText(txOpt.getText());

    }

    private void clearAllAns() {

        TxAns1.setText("");
        TxAns2.setText("");
        TxAns3.setText("");
        TxAns4.setText("");
        TxAns5.setText("");
        TxAns6.setText("");
        TxAns7.setText("");
        TxAns8.setText("");
        TxAns9.setText("");

        TxAns1.setVisibility(View.INVISIBLE);
        TxAns2.setVisibility(View.INVISIBLE);
        TxAns3.setVisibility(View.INVISIBLE);
        TxAns4.setVisibility(View.INVISIBLE);
        TxAns5.setVisibility(View.INVISIBLE);
        TxAns6.setVisibility(View.INVISIBLE);
        TxAns7.setVisibility(View.INVISIBLE);
        TxAns8.setVisibility(View.INVISIBLE);
        TxAns9.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
