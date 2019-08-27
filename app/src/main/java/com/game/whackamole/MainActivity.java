package com.game.whackamole;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    //Constants (Game Settings)
    final int MAX_MOLES_SPAWNED = 3;
    final int MAX_TIME = 30;
    final int MAX_LIVES = 3;
    final int SPAWN_TIME =1000;
    final int WINNING_SCORE=30;

    //Variables for views
    private ImageButton[] hole;
    private TextView timeTV,scoreTV;
    private View menu,game;
    private ImageButton playBtn;
    private TextView status;
    private TextView reason;


    //Game Variables
    private boolean[] holeHasMole;
    private Timer timer;
    private int time;
    private int score;
    private Random r ;
    private int lives=3;
    private boolean spawn=true;
    private String name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set to fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set view
        setContentView(R.layout.activity_main);

        //Init variables
        r=new Random();
        holeHasMole=new boolean[9];

        //Init Views
        menu=findViewById(R.id.menu);
        game=findViewById(R.id.game_layout);
        status=findViewById(R.id.statusText);
        timeTV=findViewById(R.id.timeTV);
        reason=findViewById(R.id.reasonTV);
        scoreTV=findViewById(R.id.scoreTV);

        //Set up buttons (Holes)
        hole=new ImageButton[9];
        hole[0]=findViewById(R.id.hole1);
        hole[1]=findViewById(R.id.hole2);
        hole[2]=findViewById(R.id.hole3);
        hole[3]=findViewById(R.id.hole4);
        hole[4]=findViewById(R.id.hole5);
        hole[5]=findViewById(R.id.hole6);
        hole[6]=findViewById(R.id.hole7);
        hole[7]=findViewById(R.id.hole8);
        hole[8]=findViewById(R.id.hole9);

        for(int i=0;i<9;i++){
            hole[i].setOnClickListener(this);
        }

        //Set up Play btn
        playBtn=findViewById(R.id.play_btn);
        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.setVisibility(View.GONE);
                game.setVisibility(View.VISIBLE);


                resetGame();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startGame();
                            }
                        });
                    }
                };
                timer = new Timer();
                timer.schedule(task, 1000);

            }
        });


        //Get the name from the Entry Activity
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

    }


    //This functions resets the game variables
    public void resetGame(){
        for(int i=0;i<9;i++){
            holeHasMole[i]=false;
        }
        //Inistialize game variables
        time=MAX_TIME; //Set time to 30seconds
        score=0;
        lives=MAX_LIVES;

        updateUI();
        updateTimeText();
        scoreTV.setText("Score:"+score);
    }


    //This functions removes or spawns moles
    public void spawnMoles(){

        if(spawn) {


            //pick an number between 2 and MAX_MOLES_SPAWNED
            int numberOfMoles=r.nextInt(MAX_MOLES_SPAWNED-1)+2;

            //Spawns "numberOfMoles" moles
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i=1; i<9; i++) {
                list.add(new Integer(i));
            }
            Collections.shuffle(list);
            for (int i=0; i<numberOfMoles; i++) {
               holeHasMole[list.get(i)] = true;
            }



        }else{
            //Remove all moles
            for(int i=0;i<9;i++){
                holeHasMole[i]=false;
            }
        }
        spawn=!spawn;
        updateUI();
    }

    //Updates the UI,changes the images based on the holeHasMole variable
    public void updateUI(){
        for(int i=0;i<9;i++){
            if(holeHasMole[i]){
                hole[i].setImageResource(R.drawable.hole_with_mole);
            }else{
                hole[i].setImageResource(R.drawable.hole_without_mole);
            }
        }
    }

    //Starts the game,set up the timer
    public void startGame(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTimer();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, new Date(), 1000);


        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spawnMoles();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task2, new Date(), SPAWN_TIME);

    }

    //Check if the hole has a mole or not
    public void checkHole(int i){
        if(holeHasMole[i]){
            score++;

            holeHasMole[i]=false;
            hole[i].setImageResource(R.drawable.hole_correct);

        }else{
           lives--;

            updateTimeText();
            hole[i].setImageResource(R.drawable.hole_false);
        }
        checkGameOver();

        scoreTV.setText("Score:"+score);
    }

    //Updates the time text view
    public void updateTimeText(){
        timeTV.setText("Time:"+time+"sec"+"\nLives:"+lives);
    }

    //Changs the view visibility to gameover
    public void gameOver(String r){
        timer.cancel();
        menu.setVisibility(View.VISIBLE);
        status.setText("Game Over");
        reason.setText(name+r);

        game.setVisibility(View.GONE);
    }

    //Checks if the game is over
    public void checkGameOver(){
        //Check if time is up
        if(time<=0){
            gameOver(",your time is up!");

        }
        //Check the number of lives
        if(lives<=0){
            gameOver(",you have missed three times.");
        }
        //Check the score
        if(score>=WINNING_SCORE){
            gameOver(",you won!");
        }
    }

    //Update the timer
    public void updateTimer(){
        time--;

        updateTimeText();
        checkGameOver();

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.hole1:
                checkHole(0);
                break;
            case R.id.hole2:
                checkHole(1);
                break;
            case R.id.hole3:
                checkHole(2);
                break;
            case R.id.hole4:
                checkHole(3);
                break;
            case R.id.hole5:
                checkHole(4);
                break;
            case R.id.hole6:
                checkHole(5);
                break;
            case R.id.hole7:
                checkHole(6);
                break;
            case R.id.hole8:
                checkHole(7);
                break;
            case R.id.hole9:
                checkHole(8);
                break;
        }
    }
}
