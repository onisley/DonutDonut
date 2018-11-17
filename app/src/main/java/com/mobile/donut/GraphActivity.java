package com.mobile.donut;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GraphActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String connectCode;

    //private EditText editYear;
    //private int monthCost[];
    //private int monthSave[];
    private int index;

    private String[] items;
    private int[] itemSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String[] data=getResources().getStringArray(R.array.기간);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        connectCode = intent.getStringExtra("connectCode");

        //editYear =(EditText)findViewById(R.id.editYear);
        //monthCost = new int[13];
        //monthSave = new int[13];

        items = new String[4];
        items[0] = "수입";
        items[1] = "식비";
        items[2] = "여가비";
        items[3] = "기타";

        itemSum = new int[4];
        for (int i = 0; i<4; i++){
            itemSum[i] = 0;
        }

        // 그래프그리기
        // 2개월전/1개월전/현재
        int[] max_points = {9,7,5,6,4,8};  // 수입그래프
        int[] min_points = {3,6,6,7,5,4};  // 지출그래프 - 식비, 여가비, 기타 순
        // itemSum[1],itemSum[2],itemSum[3]

        GraphView graphview = (GraphView) findViewById(R.id.GraphView);

        //단위는 1씩, 원점은 0, 총 10줄로 나누어진 그래프를 그린다
        graphview.setPointsMax(max_points, 1, 0, 10);
        graphview.setPointsMin(min_points, 1, 0, 10);
        graphview.drawForBeforeDrawViewMax();   // 수입
        graphview.drawForBeforeDrawViewMin();   // 지출
    }

    public void onBtnShow(View view) {
        /*String year = editYear.getText().toString();
        Query query = db.collection(connectCode).whereEqualTo("data", true).whereEqualTo("year", year);

        for (index = 1; index <= 12; index++) {
            monthCost[index - 1] = 0;
            monthSave[index - 1] = 0;
        }

        for (index = 1; index <= 12; index++) {
            Query query2 = db.collection(connectCode).whereEqualTo("data", true).whereEqualTo("year", year).whereEqualTo("month", Integer.toString(index));
            query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("donutdonut", document.getId());
                            if(document.getString("item").equals("수입")){
                                monthSave[index-1] += Integer.parseInt(document.getString("price"));
                            } else {
                                monthCost[index-1] += Integer.parseInt(document.getString("price"));
                            }
                        }
                    } else {
                        Log.d("donutdonut", "Error:task is not");
                    }
                }
            });

            Log.d("donutdonut monthCost: "+index, Integer.toString(monthCost[index-1]));
            Log.d("donutdonut monthSave: "+index, Integer.toString(monthSave[index-1]));
        }
    }

    public void onBtnNew(View view) {
        for (index = 1; index <= 12; index++) {
            Log.d("donutdonut monthCost: " + index, Integer.toString(monthCost[index - 1]));
            Log.d("donutdonut monthSave: " + index, Integer.toString(monthSave[index - 1]));
        }*/


        for (int i = 0; i<4; i++){
            itemSum[i] = 0;
        }

        for (index = 0; index <4; index++) {
            Query query = db.collection(connectCode).whereEqualTo("item", items[index]);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            itemSum[index] += Integer.parseInt(document.getString("price"));
                        }
                    } else{

                    }
                }
            });
            Log.d("donutdonut", Integer.toString(itemSum[index]));
        }
    }
}
