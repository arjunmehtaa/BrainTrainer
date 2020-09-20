package com.arjuj.braintrainerapp;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private void shakeItBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(60);
        }
    }


    private void shakeItHardBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(200);
        }
    }

    private void shakeItSoftBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(50);
        }
    }
    Button startButtonID;
    ArrayList<Integer> answers=new ArrayList<Integer>();

    TextView textView1;
    ImageView brainImage;
    int locationCorrect;
    TextView reviewText;
    int score=0;
    int counter=0;
    TextView scoreText;
    Button choice1;
    Button choice2;
    Button choice3;
    Button choice4;
    TextView questionText;
    CountDownTimer cdTimer;
    TextView secondsText;
    LinearLayout linearLayout;
    GridLayout gridLayout;
    MediaPlayer mplayer;
    boolean inPlay=false;


    @Override
    protected void onPause() {
        super.onPause();
        cdTimer.cancel();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if(inPlay==true) {
            cdTimer.start();
        }
    }

    @Override
    public void onBackPressed() {
        if(inPlay==true){
            cdTimer.cancel();
            new AlertDialog.Builder(this)
                    .setTitle("Exit Game")
                    .setMessage("Are you sure you want to exit the game?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            textView1.setText("Press the start button to train");
                            startButtonID.setText("S T A R T");
                            startButtonID.setVisibility(View.VISIBLE);
                            textView1.setVisibility(View.VISIBLE);
                            brainImage.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.INVISIBLE);
                            gridLayout.setVisibility(View.INVISIBLE);
                            questionText.setVisibility(View.INVISIBLE);
                            reviewText.setVisibility(View.INVISIBLE);
                            cdTimer.cancel();
                            inPlay=false;
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cdTimer.start();
                        }

                    })
                    .show();



        }
        else{
            finish();
        }
    }





    public void startButton(View view) {
        shakeItSoftBaby();
        startButtonID.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        brainImage.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);
        questionText.setVisibility(View.VISIBLE);
        reviewText.setVisibility(View.VISIBLE);
        inPlay=true;
        newQuestion();
        startCount();
        counter=0;
        score=0;
        scoreText.setText(Integer.toString(score) + "/" + Integer.toString(counter));
        reviewText.setText("");

    }



    public void chooseAnswer(View view){

        counter++;
        if(counter<10) {
            if (Integer.toString(locationCorrect).equals(view.getTag().toString())) {
                reviewText.setText("Right Answer!");
                shakeItSoftBaby();
                score++;
                cdTimer.cancel();
                mplayer=MediaPlayer.create(getApplicationContext(),R.raw.correct);
                mplayer.start();

            } else {
                reviewText.setText("Wrong Answer!");
                cdTimer.cancel();
                shakeItBaby();
                mplayer=MediaPlayer.create(getApplicationContext(),R.raw.wrong);
                mplayer.start();
            }
            scoreText.setText(Integer.toString(score) + "/" + Integer.toString(counter));

            newQuestion();
            startCount();
        }
        else {
            if (Integer.toString(locationCorrect).equals(view.getTag().toString())) {
                score++;
                mplayer=MediaPlayer.create(getApplicationContext(),R.raw.correct);
                mplayer.start();
                shakeItSoftBaby();
            }
            else{
                mplayer=MediaPlayer.create(getApplicationContext(),R.raw.wrong);
                mplayer.start();
                shakeItBaby();
            }
            textView1.setText("Your score was "+Integer.toString(score) + " out of " + Integer.toString(counter));
            startButtonID.setText("P L A Y   A G A I N");
            startButtonID.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            brainImage.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);
            gridLayout.setVisibility(View.INVISIBLE);
            questionText.setVisibility(View.INVISIBLE);
            reviewText.setVisibility(View.INVISIBLE);
            cdTimer.cancel();
            inPlay=false;


        }




    }

    public void startCount() {
        cdTimer=new CountDownTimer(20000+100,1000) {
            @Override
            public void onTick(long l) {
                secondsText.setText(Integer.toString((int)l/1000)+"s");
            }

            @Override
            public void onFinish() {
                reviewText.setText("Time Out!");
                newQuestion();
                counter++;
                scoreText.setText(Integer.toString(score)+"/"+Integer.toString(counter));
                startCount();
                mplayer=MediaPlayer.create(getApplicationContext(),R.raw.wrong);
                mplayer.start();

            }
        }.start();

    }


    public void newQuestion() {

        Random rand=new Random();

        int a=rand.nextInt(21)+1;
        int b=rand.nextInt(21)+1;

        locationCorrect=rand.nextInt(4);

        answers.clear();

        for(int i=0;i<4;i++) {
            if (i == locationCorrect) {
                answers.add(a + b);
            }
            else{
                int wrongAnswer=rand.nextInt(40)+1;
                while(wrongAnswer==a+b) {
                    wrongAnswer=rand.nextInt(40)+1;
                }
                answers.add(wrongAnswer);
            }
        }

        questionText.setText(Integer.toString(a)+"+"+Integer.toString(b));
        choice1.setText(Integer.toString(answers.get(0)));
        choice2.setText(Integer.toString(answers.get(1)));
        choice3.setText(Integer.toString(answers.get(2)));
        choice4.setText(Integer.toString(answers.get(3)));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.txt_layout);


        choice1=(Button) findViewById(R.id.choice1);
        choice2=(Button) findViewById(R.id.choice2);
        choice3=(Button) findViewById(R.id.choice3);
        choice4=(Button) findViewById(R.id.choice4);
        startButtonID=(Button)findViewById(R.id.startButtonID);
        textView1=(TextView)findViewById(R.id.textView1);
        brainImage=(ImageView)findViewById(R.id.brainImage);
        questionText=(TextView)findViewById(R.id.questionText);
        reviewText=(TextView)findViewById(R.id.reviewText);
        scoreText=(TextView)findViewById(R.id.scoreText);
        secondsText=(TextView)findViewById(R.id.secondsText);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        gridLayout=(GridLayout)findViewById(R.id.gridLayout);









    }
}
