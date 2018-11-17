package com.mobile.donut;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingActivity extends AppCompatActivity {
    private String connectCode;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;

    String id;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        connectCode = intent.getStringExtra("connectCode");

        // 연결된 사용자의 이름
        db = FirebaseFirestore.getInstance();

        // 사용자의 이름과 아이디
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db.collection(user.getUid()).document("userInfo").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    name = document.getString("Name");
                    id = document.getString("ID");
                }
            }
        });

    }

    public void onBtnInfo(View view) {  // 내 정보
        AlertDialog.Builder appIntro = new AlertDialog.Builder(this);
        appIntro.setTitle("내 정보");
        appIntro.setMessage("이름: " + name + "\n아이디: " + id);

        appIntro.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = appIntro.create();  // 대화상자 객체 생성후 보여주기
        dialog.show();
    }

    public void onBtnSetCon(View view) {   // 연결관리
        // 연결끊기
        AlertDialog.Builder disconnect = new AlertDialog.Builder(this);
        disconnect.setTitle("연결 끊기");
        disconnect.setMessage("상대방과의 연결을 끊으시겠습니까?");

        disconnect.setPositiveButton("예", new DialogInterface.OnClickListener() {   // 예
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.collection(connectCode).document("userInfo").update("isShared", false, "connectCode", "");

                Toast.makeText(getApplicationContext(), "상대방과의 연결이 끊어졌습니다.",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), LinkActivity.class);
                startActivity(intent);  // 초기화면으로
                finish();
            }
        });

        disconnect.setNegativeButton("아니오", new DialogInterface.OnClickListener() { // 아니오
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = disconnect.create();  // 대화상자 객체 생성후 보여주기
        dialog.show();
    }

    public void onBtnAppInfo(View view) {   // 개발자 정보
        //Intent intent = new Intent(getApplication(), IntrodevelopActivity.class);


        //startActivity(intent);
    }
}
