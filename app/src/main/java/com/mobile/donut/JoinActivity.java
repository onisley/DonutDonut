package com.mobile.donut;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {
    public static String TAG = "DonutDonut";

    private FirebaseAuth mAuth;

    private EditText editID;
    private EditText editPW;
    private EditText editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mAuth = FirebaseAuth.getInstance();

        editID = (EditText)findViewById(R.id.editID);
        editPW = (EditText)findViewById(R.id.editPW);
        editName = (EditText)findViewById(R.id.editName);
    }

    public void onBtnJoin(View view) {
        final String ID = editID.getText().toString();
        final String PW = editPW.getText().toString();
        final String Name = editName.getText().toString();

        if(ID != "" && PW != "" && Name != "") {
            mAuth.createUserWithEmailAndPassword(ID, PW)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 회원가입 성공 시 사용자 데이터 생성
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(JoinActivity.this, "성공적으로 가입되었습니다", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                InitializeUser(user, ID, PW, Name);
                                finish();
                            } else {
                                // 회원가입 실패
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(JoinActivity.this, "회원가입에 실패하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(JoinActivity.this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public static void InitializeUser(FirebaseUser user, String ID, String PW, String Name) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("ID", ID);
        userData.put("PW", PW);
        userData.put("Name", Name);
        userData.put("isShared",false);
        userData.put("connectID","");

        Map<String, Object> userIndex = new HashMap<>();
        userIndex.put("indexStart", "0");
        userIndex.put("indexEnd", "0");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(user.getUid()).document("userInfo").set(userData);
        db.collection(user.getUid()).document("index").set(userIndex);
    }
}
