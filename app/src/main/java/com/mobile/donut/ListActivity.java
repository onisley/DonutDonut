package com.mobile.donut;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    public static final String TAG = "DonutDonut";

    // 리스트 멤버변수
    ListView listView;
    ListAdapter listAdapter;
    private TextView lastMoney;
    private CustomDialog dialog;

    // 파이어베이스 멤버변수
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private static String connectCode; // DB 접근 코드

    // 데이터 값
    String item;
    String content;
    String price;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // 파이어베이스 멤버 변수 초기화
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        connectCode = intent.getStringExtra("connectCode");

        // 리스트 멤버 변수 초기화
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ListAdapter();
        lastMoney = (TextView)findViewById(R.id.lastMoneyText);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItemClicked(position);
            }
        });
        dialog = new CustomDialog(this,"", listAdapter);

        updateUI(); // DB 내용을 읽어 리스트뷰 갱신
    }

    // 버튼 이벤트에 따라서 항목들을 리스트에 동적 추가
    public void onBtnAdd(View view) {
        CustomDialog dialog = new CustomDialog(this,"", listAdapter);
        dialog.show();  // 사용자 지정 다이얼로그 보이기
    }

    /* 새로 정의한 메소드 */
    public static String getConnectID() { // DB 접근 코드를 반환(CustomDialog에서 DB에 접근하기 위해 호출)
        return connectCode;
    }

    private void updateUI(){ // DB에서 데이터를 읽어 리스트뷰 갱신
        listAdapter.deleteAll();
        listAdapter.notifyDataSetChanged();

        // DB 접근 코드로 data에 해당하는 값들 query -> 날짜순, 항목별 정렬하여 순차적으로 리스트뷰에 등록
        CollectionReference colRef = db.collection(connectCode);
        Query query = colRef.whereEqualTo("data",true).orderBy("year").orderBy("month").orderBy("date");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Log.d(TAG, document.getId() + " => " + document.getString("item"));

                        String tag = document.getId().toString();
                        Log.d(TAG, tag);
                        item = document.getString("item");
                        date = document.getString("date");
                        content = document.getString("content");
                        price = document.getString("price");

                        // 항목(수입, 지출)에 따라 잔액을 계산하여 상단 TextView에 표시
                        int now = Integer.parseInt(lastMoney.getText().toString());
                        int sum = getSum(item, now, Integer.parseInt(price));
                        lastMoney.setText(Integer.toString(sum));

                        ListItem newOne = new ListItem(item, date, content, price, Integer.toString(sum));
                        newOne.setTag(tag);
                        listAdapter.addItem(newOne);
                        listAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private int getSum(String item, int sum, int price) { // 항목(수입, 지출)에 따라 잔액을 계산하여 반환하는 메소드
        if(item.equals("수입")){
            return sum + price;
        } else {
            return sum - price;
        }
    }

    private void listItemClicked(int position) { // 리스트뷰의 아이템을 클릭했을 경우 삭제 다이얼로그 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("해당 항목을 삭제하시겠습니까?");
        builder.setIcon(R.drawable.icon_heart);

        final int pos = position;

        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListItem item = (ListItem) listAdapter.getItem(pos);

                String tag = item.getTag();
                Log.d(TAG, Integer.toString(pos));
                Log.d(TAG, tag);

                // 선택한 항목의 tag와 일치하는 DB 데이터를 삭제
                db.collection(connectCode).document(tag).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                listAdapter.removeItem(pos);
                listAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* 새로 정의한 클래스 */
    class ListAdapter extends BaseAdapter {
        ArrayList<ListItem> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(ListItem item) {
            items.add(item);
        }

        public void removeItem(int pos) {
            items.remove(pos);
        }

        protected void deleteAll() {
            items.clear();
        }

        @Override // ****** 중요함 *******
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView view = new ListItemView(getApplicationContext());
            ListItem item = items.get(position);
            view.setDate(item.getDate());
            view.setItem(item.getItem());
            view.setPrice(item.getPrice());
            view.setContent(item.getContent());
            view.setTotal(item.getTotal());

            return view;
        }
    }
}
