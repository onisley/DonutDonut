package com.mobile.donut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "DonutDonut";

    public static final int REQUEST_CODE_LIST = 101;
    public static final int REQUEST_CODE_GRAPH = 102;
    public static final int REQUEST_CODE_ALARM = 103;
    public static final int REQUEST_CODE_SAVE = 104;
    public static final int REQUEST_CODE_SET = 105;

    private String connectCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        connectCode = intent.getStringExtra("connectCode");
    }

    public void onBtnList(View view) {
        Intent intent = new Intent(getApplication(), ListActivity.class);
        intent.putExtra("connectCode", connectCode);

        startActivityForResult(intent,REQUEST_CODE_LIST);
    }

    public void onBtnGraph(View view) {
        Intent intent = new Intent(getApplication(), GraphActivity.class);
        intent.putExtra("connectCode", connectCode);

        startActivityForResult(intent,REQUEST_CODE_GRAPH);
    }

    public void onBtnAlarm(View view) {
        Intent intent = new Intent(getApplication(), AlarmActivity.class);
        intent.putExtra("connectCode", connectCode);

        startActivityForResult(intent,REQUEST_CODE_ALARM);
    }

    public void onBtnSave(View view) {
        Intent intent = new Intent(getApplication(), IntroActivity.class);
        intent.putExtra("connectCode", connectCode);

        startActivityForResult(intent, REQUEST_CODE_SAVE);
    }

    public void onBtnSetting(View view) {
        Intent intent = new Intent(getApplication(), SettingActivity.class);
        intent.putExtra("connectCode", connectCode);

        startActivityForResult(intent,REQUEST_CODE_SET);
    }

}
