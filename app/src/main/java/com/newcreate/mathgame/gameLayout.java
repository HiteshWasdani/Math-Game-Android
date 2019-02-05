package com.newcreate.mathgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class gameLayout extends AppCompatActivity {

    int time = 30100;
    Random random;
    Button b1;   Button b2;   Button b3;    Button b4;
    TextView correctTextView;
    TextView totalTextView;
    TextView questionTextView;
    TextView timerTextView;
    ArrayList<Integer> positions;
    Button replayButton;
    CountDownTimer countDownTimer;

    int pos;
    int correct =0;
    int total =0;
    char operator;
    Boolean first = true;
    int operationTag;


    public void startTimer()
    {
        countDownTimer = new CountDownTimer(time,1000) {

            @Override
            public void onTick(long l)
            {
                if(l/1000 < 11 )   timerTextView.setTextColor(Color.RED);

                time = (int)l;
                timerTextView.setText(String.valueOf(l/1000)+"s");
            }

            @Override
            public void onFinish()
            {
                if(timerTextView.getText().toString().equals("1s"))
                {
                    timerTextView.setText("00");
                    b1.setEnabled(false);        b2.setEnabled(false);
                    b3.setEnabled(false);        b4.setEnabled(false);
                    replayButton.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }


    public void pauseTime()
    {
        countDownTimer.cancel();
        countDownTimer.onFinish();
        b1.setEnabled(false);        b2.setEnabled(false);
        b3.setEnabled(false);        b4.setEnabled(false);
    }

    public void resumeTime()
    {
        countDownTimer.cancel();
        countDownTimer.onFinish();
        time = time - 1000;
        startTimer();
    }

    public void delayTime()
    {

        new CountDownTimer(500, 100) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                newQuestion();
            }
        }.start();
    }


    public void newQuestion()
    {
        if(first == true)  {  startTimer(); first = false; }
        else if(!(time < 1100)) resumeTime();
        else if(time<1100)   {countDownTimer.onFinish(); return;}

        b1.setEnabled(true);       b2.setEnabled(true);
        b3.setEnabled(true);       b4.setEnabled(true);

        if(operationTag == 3)  checkOperator();

        b1.setBackgroundColor(Color.WHITE);     b2.setBackgroundColor(Color.WHITE);
        b3.setBackgroundColor(Color.WHITE);     b4.setBackgroundColor(Color.WHITE);

        int a = 0;
        int b = 0;

        while(a==0)  a = random.nextInt(21);
        while(b==0)  b = random.nextInt(21);

        int result=0, range = 0;

        switch (operator)
        {
            case '+': result = a + b; range = 21+21; break;
            case '-':
                    while(a==0) a = random.nextInt(41);
                    while(b>a)  b = random.nextInt(41);

                    result = a - b;
                    range = 41+41;

                    break;
            case '*': result = a * b; range = 21*21; break;
            default:
        }

        questionTextView.setText(Integer.toString(a) +" "+ operator +" "+ Integer.toString(b)+" = ?");

        pos = random.nextInt(4);
        for(int i=0; i<=3; i++)
        {
            if(i == pos)
             {    positions.add(result); continue;}

            else
            {
                int temp = random.nextInt(range);

                while(temp == result )
                    temp = random.nextInt(range);

                positions.add(temp);
            }
        }

        b1.setText(positions.get(0).toString());       b3.setText(positions.get(2).toString());
        b2.setText(positions.get(1).toString());       b4.setText(positions.get(3).toString());

    }


    public void checkAnswer(View v)
    {
        pauseTime();

        if(String.valueOf(pos).equals(b1.getTag().toString()))  b1.setBackgroundColor(Color.GREEN);
        else if(String.valueOf(pos).equals(b2.getTag().toString()))  b2.setBackgroundColor(Color.GREEN);
        else if(String.valueOf(pos).equals(b3.getTag().toString()))  b3.setBackgroundColor(Color.GREEN);
        else if(String.valueOf(pos).equals(b4.getTag().toString()))  b4.setBackgroundColor(Color.GREEN);
        else Toast.makeText(this,"toast",Toast.LENGTH_SHORT).show();

        positions.clear();

        int temp = Integer.parseInt(v.getTag().toString());

        if(temp != pos)
            {
                if(v.getId() == R.id.b1)  b1.setBackgroundColor(Color.RED);
                else if(v.getId() == R.id.b2)  b2.setBackgroundColor(Color.RED);
                else if(v.getId() == R.id.b3)  b3.setBackgroundColor(Color.RED);
                else if(v.getId() == R.id.b4)  b4.setBackgroundColor(Color.RED);
            }

        delayTime();
        if(temp == pos) correctTextView.setText(Integer.toString(++correct));

        totalTextView.setText(Integer.toString(++total));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_layout);

//        time = 15100;
        random = new Random();
        timerTextView = findViewById(R.id.timerTextView);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        replayButton = findViewById(R.id.replayButton);
        replayButton.setText("Replay");
        replayButton.setVisibility(View.INVISIBLE);
        positions = new ArrayList<>();
        correctTextView = findViewById(R.id.correctTextView);
        totalTextView = findViewById(R.id.totalTextView);
        questionTextView = findViewById(R.id.questionTextView);

        Intent intent = getIntent();
        operationTag = intent.getIntExtra("operationTag",-1);

        checkOperator();
        newQuestion();
    }

    public void checkOperator()
    {
        switch (operationTag)
        {
            case 0:  operator = '+';    break;
            case 1:  operator = '-';    break;
            case 2:  operator = '*';    break;
            case 3:  int t = random.nextInt(3);
                     if(t == 0)  operator = '+';
                     else if(t == 1) operator = '-';
                     else if(t == 2) operator = '*';
                     break;

            default: break;
        }
    }

    public void replayButtonClicked(View v)
    {
        finish();
    }
}
