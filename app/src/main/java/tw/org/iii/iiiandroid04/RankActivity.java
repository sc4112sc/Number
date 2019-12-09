package tw.org.iii.iiiandroid04;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RankActivity extends AppCompatActivity {

    private F1 f1;
    private F2 f2;
    private F3 f3;
    private F4 f4;

    private FragmentManager fmgr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);



        f1 = new F1();
        f2 = new F2();
        f3 = new F3();
        f4 = new F4();

        fmgr = getSupportFragmentManager();
        FragmentTransaction transaction = fmgr.beginTransaction();
        transaction.add(R.id.rank_container,f1);
        transaction.commit();
    }

    public void toF1(View view) {
        FragmentTransaction transaction = fmgr.beginTransaction();
        transaction.replace(R.id.rank_container, f1);
        transaction.commit();
    }

    public void toF2(View view) {
        FragmentTransaction transaction = fmgr.beginTransaction();
        transaction.replace(R.id.rank_container, f2);
        transaction.commit();
    }

    public void toF3(View view) {
        FragmentTransaction transaction = fmgr.beginTransaction();
        transaction.replace(R.id.rank_container, f3);
        transaction.commit();

    }

    public void toF4(View view) {
        FragmentTransaction transaction = fmgr.beginTransaction();
        transaction.replace(R.id.rank_container, f4);
        transaction.commit();
    }
}
