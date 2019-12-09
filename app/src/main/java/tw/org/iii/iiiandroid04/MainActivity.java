package tw.org.iii.iiiandroid04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.view.ViewCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity  {

    private  String answer;
    private  int dig = 3,tmp=0;
    private EditText input;
    //private TextView log;
    private int counter;
    private long lastTime = 0;


    private ViewFlipper viewFlipper;
    //private TextView clock;
    private ListView myListView;
    private SimpleAdapter adapter;
    private LinkedList<HashMap<String,String>> data;
    private String[] from = {"myItem"};
    private int[] to = {R.id.myItem};

    private StringBuffer sb1 = new StringBuffer();


    private Timer timer;
    private int i;
    private String score;

    private  UIHandler uiHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = findViewById(R.id.viewFlipper);
        //clock = findViewById(R.id.clock);
        input = findViewById(R.id.input);
        //log = findViewById(R.id.log);
        answer = createAnswer(dig);
        myListView = findViewById(R.id.myList);
        input.setEnabled(false);
        input.setText("請猜出"+dig+"碼數字");

        i=10;
        viewFlipper.setDisplayedChild(9);

        timer = new Timer();
        uiHandler = new UIHandler();
        timer.schedule(new MyTask(),0,1000);

        initListView();











    }

    private  void initListView(){
        data = new LinkedList<>();
        adapter = new SimpleAdapter(this,data,R.layout.item,from,to);
        myListView.setAdapter(adapter);
    }

    private void putIn(String ss){
        HashMap<String,String> itemData = new HashMap<>();
        itemData.put(from[0],ss);
        data.add(0,itemData);
        adapter.notifyDataSetChanged();

    }

    //產生謎底
    private String createAnswer(int dig){

//        HashSet<Integer> set = new HashSet<>();
//        while (set.size()<dig){
//            set.add((int)(Math.random()*10));
//        }
//        StringBuffer sb = new StringBuffer();
//        for(Integer i : set){
//            sb.append(i);
//        }
//        Log.v("scott",sb.toString());
        LinkedList<Integer> list = new LinkedList<>();
        for (int i=0;i<10;i++){
            list.add(i);
        }
        Collections.shuffle(list);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<dig;i++){
            sb.append(list.get(i));
        }
        Log.v("scott",sb.toString());
        return sb.toString();
    }

    public void exit(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Exit?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        alertDialog.show();
    }



    @Override
    public void finish() {

        if(timer!=null){
            timer.cancel();
            timer.purge();
            timer=null;
        }

        super.finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);

    }

    @Override
    public void onBackPressed() {

        //Log.v("scott","onBackPressed");
        if(System.currentTimeMillis() - lastTime> 3*1000){
            lastTime = System.currentTimeMillis();
            Toast.makeText(this,"back one more", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }


    }

    public void setting(View view) {
        String items[] = {"3","4","5","6"};

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(items, dig-3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("scott","which1 = "+which);
                        tmp = which;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dig = tmp + 3;
                        Log.v("scott","dig = "+dig);

                        newGame(null);
                    }
                })
                .create();
        alertDialog.show();
    }

    public void newGame(View view) {
        //Log.v("scott","new game");
        timer.cancel();

        i=11;
        viewFlipper.setDisplayedChild(9);
        counter = 0;
        timer = new Timer();
        timer.schedule(new MyTask(),0,1000);

        input.setText("請猜出"+dig+"碼數字");
        //log.setText("");
        int last = sb1.length();
        sb1.delete(0,last);
        data.clear();
        adapter.notifyDataSetChanged();
        answer = createAnswer(dig);




    }

    public void guess(View view) {

        String str1 = input.getText().toString();
        if (!isRightNumber(str1)){
            return;
        }
        counter++;
        String result = checkAB(str1);
        String str2 = counter+" : " +str1 + " => "+ result;
        putIn(str2);

        i = 11;
        viewFlipper.setDisplayedChild(0);

        if(result.equals(dig+"A0B")){
            showDialog(true);
        }else if (counter==10){
            showDialog(false);
        }
        input.setText("請猜出"+dig+"碼數字");
        sb1.delete(0,dig);


    }

    private  boolean isRightNumber(String g){

        return  g.matches("^[0-9]{" + dig + "}$");

    }

    private  void showDialog(boolean isWinner){

        score = String.valueOf(10000 - (counter*1000));

        timer.cancel();

        final Intent intent = new Intent(this,OverActivity.class);

        intent.putExtra("wol",isWinner);
        intent.putExtra("dig",dig);
        intent.putExtra("score",score);

        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isWinner?"WINNER":"LOSER");
        builder.setMessage(isWinner?"恭喜恭喜":"答案為"+answer);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(intent);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private  String checkAB(String guess){
        int a, b;
        a = b = 0;
        for(int i=0;i<guess.length(); i++){
            if(guess.charAt(i) == answer.charAt(i)){
                a++;
            }else if(answer.indexOf(guess.charAt(i))>=0){
                b++;
            }
        }
        return a + "A" + b + "B";
    }



    public void zero(View view) {
        if (sb1.length() < dig){
            sb1.append("0");
            input.setText(sb1);
        }

    }

    public void one(View view) {
        if (sb1.length() < dig){
            sb1.append("1");
            input.setText(sb1);
        }


    }

    public void two(View view) {
        if (sb1.length() < dig){
            sb1.append("2");
            input.setText(sb1);
        }
    }

    public void three(View view) {
        if (sb1.length() < dig){
            sb1.append("3");
            input.setText(sb1);
        }
    }

    public void four(View view) {
        if (sb1.length() < dig){
            sb1.append("4");
            input.setText(sb1);
        }
    }

    public void five(View view) {
        if (sb1.length() < dig){
            sb1.append("5");
            input.setText(sb1);
        }
    }

    public void six(View view) {
        if (sb1.length() < dig){
            sb1.append("6");
            input.setText(sb1);
        }
    }

    public void seven(View view) {
        if (sb1.length() < dig){
            sb1.append("7");
            input.setText(sb1);
        }
    }

    public void eight(View view) {
        if (sb1.length() < dig){
            sb1.append("8");
            input.setText(sb1);
        }
    }

    public void nine(View view) {
        if (sb1.length() < dig){
            sb1.append("9");
            input.setText(sb1);
        }
    }

    public void backOne(View view) {
        if (sb1.length()>0){
            int last = sb1.length()-1;
            sb1.deleteCharAt(last);
            input.setText(sb1);
        }else {

        }

        if (sb1.length()==0){
            input.setText("請猜出"+dig+"碼數字");
        }


    }

    public void clearAll(View view) {

        if (sb1.length()==0){

        }else {
            int last = sb1.length();
            sb1.delete(0,last);
            input.setText(sb1);
        }
        input.setText("請猜出"+dig+"碼數字");

    }



    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //在這執行緒內執行ui更新

            if(i == 0){
                i = 10;
                counter++;
                String str = counter+" :  YOU MISS";
                putIn(str);
                viewFlipper.setDisplayedChild(9);

            }

            if (counter==10){
                showDialog(false);
            }


            //clock.setText(""+i);
            viewFlipper.showNext();
        }
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            i--;

            uiHandler.sendEmptyMessage(0);

        }
    }
}
