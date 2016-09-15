package org.ghost;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {

    private TextView countDown;
    private Button start;
    private Button pause;
    private Timer myTimer;

    int count = 60;
    boolean isPause = false;
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        getView();

        Toast.makeText(TimerActivity.this,"Timer 測試",Toast.LENGTH_SHORT).show();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTimer.cancel();
                isPause = true;
                isStart = false;
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStart){
                    myTimer = new Timer();
                    setTimerTask();
                    isStart=true;
                }

            }
        });

        Toast.makeText(TimerActivity.this,"onCreate",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(TimerActivity.this,"onStart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isPause){
            myTimer = new Timer();
            setTimerTask();
            isStart = true;
        }else{
            myTimer.cancel();
            isPause=false;
        }

        Toast.makeText(TimerActivity.this,"onResume",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTimer.cancel();
        Toast.makeText(TimerActivity.this,"onPause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(TimerActivity.this,"onStop",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(TimerActivity.this,"onRestart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(TimerActivity.this,"onDestroy",Toast.LENGTH_SHORT).show();
    }

    private void setTimerTask(){
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                myHandler.sendMessage(msg);
            }
        },1000,1000);
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgID = msg.what;
            switch (msgID) {
                case 1:
                    if(count>0){
                        count--;
                        countDown.setText(count+"");
                    }else {

                            myTimer.cancel();
                            new AlertDialog.Builder(TimerActivity.this).setMessage("時間到!!")
                                    .setTitle("訊息")
                                    .setPositiveButton("OK",null)
                                    .show();
                    }
                    break;
                default:
                    Toast.makeText(TimerActivity.this, "未傳遞", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void getView() {
        countDown = (TextView) findViewById(R.id.tv_countDown);
        start = (Button) findViewById(R.id.btn_start);
        pause = (Button) findViewById(R.id.btn_pause);
    }


}
