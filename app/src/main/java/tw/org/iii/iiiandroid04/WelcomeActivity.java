package tw.org.iii.iiiandroid04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tv1;
    private Timer timer;
    private UIHandler uiHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tv1 = findViewById(R.id.startText);

        timer = new Timer();
        uiHandler = new UIHandler();
        timer.schedule(new MyTask(),0,500);
    }

    public void gotoMain(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer!=null){
            timer.cancel();
            timer.purge();
            timer=null;
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (tv1.getAlpha() == 0){
                tv1.setAlpha(1);
            } else {
                tv1.setAlpha(0);
            }


        }
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            uiHandler.sendEmptyMessage(0);
        }
    }
}
