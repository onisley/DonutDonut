package com.mobile.donut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.google.android.gms.common.util.ClientLibraryUtils.getPackageInfo;

public class AuthActivity extends AppCompatActivity {
    public static String TAG = "DonutDonut";

    // 파이어베이스 멤버변수
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    // 로그인 화면 멤버변수
    private EditText editID;
    private EditText editPW;

    // 로그인 입력 값
    String ID;
    String PW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        editID = (EditText)findViewById(R.id.editID);
        editPW = (EditText)findViewById(R.id.editPW);
    }

    public void onBtnJoin(View view) { // 회원가입 화면으로
        Intent intent = new Intent(AuthActivity.this, JoinActivity.class);
        startActivity(intent);
    }

    public void onBtnLogin(View view) { // 로그인 시도
        ID = editID.getText().toString();
        PW = editPW.getText().toString();

        mAuth.signInWithEmailAndPassword(ID, PW)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공 시
                            Log.d(TAG, "로그인하였습니다");

                            // 로그인한 사용자 정보 불러오기
                            user = mAuth.getCurrentUser();

                            // 사용자 정보 확인 -> DB 접근 코드 불러오기
                            db = FirebaseFirestore.getInstance();
                            DocumentReference docRef =  db.collection(user.getUid()).document("userInfo");
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                            String connectID = document.getString("connectID");
                                            boolean isShared = document.getBoolean("isShared");

                                            if(connectID != "") { // 다른 사용자의 컬렉션을 공유하는 경우
                                                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                                intent.putExtra("connectCode", connectID); // 코드 = 다른 사용자의 컬렉션 이름
                                                startActivity(intent);
                                            } else if(isShared != false) { // 내 컬렉션이 공유된 경우
                                                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                                intent.putExtra("connectCode", user.getUid()); // 코드 = 내 컬렉션 이름
                                                startActivity(intent);
                                            } else { // 연결되지 않은 경우 -> 연결 화면으로
                                                Intent intent = new Intent(AuthActivity.this, LinkActivity.class);
                                                startActivity(intent);
                                            }
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                            // 로그인 실패 시
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "등록되지 않은 사용자입니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
