package tw.org.iii.iiiandroid04;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class OverActivity extends AppCompatActivity {

    private TextView title;
    private boolean myB;
    private String myScore;
    private String myNickname;
    private int myDig;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);

        title = findViewById(R.id.winText);


        myB = getIntent().getBooleanExtra("wol",false);
        String myTitle = myB?"YOU WIN !":"YOU LOSE !";
        title.setText(myTitle);

        myScore = getIntent().getStringExtra("score");
        myDig = getIntent().getIntExtra("dig",3);


        Log.v("scott",myDig+" /// "+myScore);

        if (myB == true){
            calDialog();
        }


    }

    public void gotoWel(View view) {
        Intent intent = new Intent(this,WelcomeActivity.class);
        startActivity(intent);
    }

    public void gotoRank(View view) {
        Intent intent = new Intent(this,RankActivity.class);
        startActivity(intent);
    }

    public void calDialog(){

        LayoutInflater inflater = LayoutInflater.from(this);
        final View v = inflater.inflate(R.layout.dialog_use, null);


        new AlertDialog.Builder(this)
                .setTitle("請輸入你的暱稱")
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) (v.findViewById(R.id.win_editText));

                        myNickname = editText.getText().toString();
                        if (myNickname.equals("")){
                            myNickname = "Someone";
                        }
                        Toast.makeText(getApplicationContext(), "恭喜進入排行榜：" +
                                myNickname, Toast.LENGTH_SHORT).show();
                        database(myNickname,myScore,String.valueOf(myDig));


                    }
                })
                .show();
    }


    public void database(String name,String score,String dig){


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dig).child(name);

        myRef.setValue(score);
    }








}
