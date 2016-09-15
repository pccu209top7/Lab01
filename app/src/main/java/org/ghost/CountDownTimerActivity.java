package org.ghost;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CountDownTimerActivity extends Activity {

    private TextView secondText;
    private Button pause;
    private Button keepGoing;

    private long remainSecond;
    private List<CountDownTimer> countDownTimerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_timer);

        findViews();

        countDownTimerList = new ArrayList<CountDownTimer>();

        //開始第一次倒數計時(60秒)
        startNewCountDown(60L * 1000);
    }

    private void findViews() {
        secondText = (TextView) findViewById(R.id.secondText);
        pause = (Button) findViewById(R.id.pause);
//        pause.setOnClickListener(this::onClickPause);
        keepGoing = (Button) findViewById(R.id.keepGoing);
//        keepGoing.setOnClickListener(this::onClickKeepGoing);
    }

    public void pause(View view) {
        countDownTimerList.remove(countDownTimerList.size() - 1).cancel();
    }

    public void keepGoing(View view) {
        startNewCountDown(CountDownTimerActivity.this.remainSecond);
    }

    private void startNewCountDown(long countDownStartSecondPoint) {
        countDownTimerList.add(new CountDownTimer(countDownStartSecondPoint, 1000) {
            @Override
            public void onTick(long l) {
                CountDownTimerActivity.this.remainSecond = l;
                secondText.setText(Long.toString(l / 1000));
            }

            @Override
            public void onFinish() {
                Log.i("stop == >", "now is " + CountDownTimerActivity.this.remainSecond);
            }
        }.start());
    }
}
