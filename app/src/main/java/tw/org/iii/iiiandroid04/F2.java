package tw.org.iii.iiiandroid04;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class F2 extends Fragment {

    public HashMap<String,Integer> data ;


    private ListView myListView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap.Entry<String, Integer>> infoIds;
    private ArrayList<HashMap<String,String>> al;
    private  String[] from = {"name","score"};
    private  int[] to = {R.id.rank1_nameItem,R.id.rank1_scoreItem};

    private  View mainView;
    private RankActivity rankActivity;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_f2, container, false);

        myListView = mainView.findViewById(R.id.rank2_List);
        al = new ArrayList<HashMap<String,String>>();
        fecthFirebase();


        return mainView;
    }

    public void fecthFirebase(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("4");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data = new HashMap<String,Integer>();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    data.put(String.valueOf(dsp.getKey()),Integer.valueOf(dsp.getValue().toString()));
                }


                Log.v("scott", "data2 is: " + data);

                sortData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sortData(){

        infoIds = new ArrayList<HashMap.Entry<String, Integer>>(data.entrySet());
        //根据value排序
        Collections.sort(infoIds, new Comparator<HashMap.Entry<String, Integer>>() {
            public int compare(HashMap.Entry<String, Integer> o1, HashMap.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
                //return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        //排序后
        for (int i = 0; i < infoIds.size(); i++) {

            HashMap<String,String> dd = new HashMap<>();
            dd.put(from[0],infoIds.get(i).getKey());
            dd.put(from[1],infoIds.get(i).getValue().toString());

            al.add(dd);


        }

        Log.v("scott", "排序後: " + al.toString());
        adapter = new SimpleAdapter(getActivity(), al,R.layout.rank_item,from,to);
        myListView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        rankActivity = (RankActivity)context;


    }
}
