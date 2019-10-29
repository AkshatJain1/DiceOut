package com.project.respe.diceout;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    TextView rollResult;
    TextView scoreText;

    int score;

    Random rand;

    int[] dice;
    ImageView[] imgDice;

    Scene mainScene;
    Scene anotherScene;
    ViewGroup sceneRoot;

    Transition fadeTransition;

    boolean onMainScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                rollDice(view);
            }
        });

        score = 0;

        rollResult = findViewById(R.id.rollResult);
        scoreText = findViewById(R.id.scoreText);

        rand = new Random();

        dice = new int[] {0,0,0};
        imgDice = new ImageView[] {findViewById(R.id.die1Image), findViewById(R.id.die2Image), findViewById(R.id.die3Image)};

        Toast.makeText(getApplicationContext(), "Welcome to DiceOut!", Toast.LENGTH_SHORT).show();
        // Create the scene root for the scenes in this app
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);

        // Create the scenes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mainScene = Scene.getSceneForLayout(sceneRoot, R.layout.content_main, this);

            anotherScene =
                    Scene.getSceneForLayout(sceneRoot, R.layout.main_menu, this);

            fadeTransition = new Fade();

            onMainScene = true;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void rollDice(View v) {
        rollResult.setText("Clicked");

        int die1 = rand.nextInt(6) + 1;
        int die2 = rand.nextInt(6) + 1;
        int die3 = rand.nextInt(6) + 1;

        dice[0] = die1;
        dice[1] = die2;
        dice[2] = die3;

        for(int x = 0; x < dice.length; x++) {
            String imageName = "die_"+dice[x]+".png";

            try {
                InputStream stream = getAssets().open(imageName);
                Drawable d = Drawable.createFromStream(stream, null);

                imgDice[x].setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        int sc = 0;

        if(die1 == die2 && die2 == die3) {
            sc = die1*100;
        }
        else if(die1== die2 || die1 == die3 || die2 == die3){
            sc = 50;
        }

        score+= sc;


        String result = "You rolled a " + die1 + ", a " + die2 + ", and a " + die3;

        if(sc == 0) {
            result += ". Try another roll!";
        }
        else if(sc == 50) {
            result+= ". You scored 50 points.";
        }
        else {
            result+= ". You got a triple! You scored " + sc + " points!";
        }

        rollResult.setText(result);
        scoreText.setText("Score: " + score);

        if(onMainScene)
            TransitionManager.go(anotherScene, fadeTransition);
        else
            TransitionManager.go(mainScene, fadeTransition);
        onMainScene = !onMainScene;



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
}
