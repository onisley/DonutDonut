package com.mobile.donut;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LinkActivity extends AppCompatActivity {
    public static final String TAG = "donutdonut";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private EditText editCode;
    private EditText copyCode;

    private String connectCode;
    private String connectName;

    private boolean canConnect;
    private boolean connectAlready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        editCode = (EditText) findViewById(R.id.editCode);
        copyCode = (EditText) findViewById(R.id.copyCode);

        canConnect = false;
    }

    public void onBtnSendCode(View view) {
        copyCode.setText(user.getUid());
        /*try {
            // UID를 직접 노출시키기 때문에 보안에 취약 -> 대체?
            // --> 카카오톡 링크 : 앱으로 직접 연결하는 탬플릿으로 대체 가능 (시간 여유 시)
            KakaoLink kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            KakaoTalkLinkMessageBuilder messageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            messageBuilder.addText("DonutDonut: 사용자 연결을 위해 연결창에 오른쪽 코드를 입력해주세요! --> "+mFirebaseUser.getUid());
            kakaoLink.sendMessage(messageBuilder,getApplicationContext());
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }*/
    }

    // 연결 코드 입력 시 띄우는 다이얼로그
    private void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 대화상자를 만들기 위한 빌더 객체 생성
        builder.setTitle("안내");
        builder.setMessage(connectName + " 님과 연결하시겠습니까?");
        builder.setIcon(R.drawable.icon_heart);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {  // ok 버튼을 누르면
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!connectAlready) {
                    // firestore에 다른 계정과의 연결 여부를 true로 수정
                    db.collection(connectCode).document("userInfo").update("isShared", true);
                    db.collection(user.getUid()).document("userInfo").update("connectID", connectCode);
                    Toast.makeText(getApplicationContext(), "연결되었습니다", Toast.LENGTH_LONG).show();

                    // 메인화면(기능)으로 넘어감
                    Intent intent = new Intent(LinkActivity.this, MainActivity.class);
                    intent.putExtra("connectCode", connectCode);
                    startActivity(intent);
                    finish();
                } else { // 사용자가 이미 연결되어 있는 경우
                    Toast.makeText(getApplicationContext(), "다른 사람과 연결된 사용자입니다", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();  // 대화상자 객체 생성후 보여주기
        dialog.show();
    }

    private boolean getUser(String connectCode){
        db.collection(connectCode).document("userInfo").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        canConnect = true;
                        connectName = document.getString("Name");
                        connectAlready = document.getBoolean("isShared");
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(getApplicationContext(), "존재하지 않는 사용자입니다", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        if(connectName != null)
            return true;
        else
            return false;
    }

    public void onBtnRcvCode(View view) {
        connectCode = editCode.getText().toString();
        canConnect = getUser(connectCode);

        if (canConnect) {
            showMessage();
        }
    }
}
