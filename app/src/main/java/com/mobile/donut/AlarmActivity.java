package com.mobile.donut;

        import android.app.DatePickerDialog;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.annotation.RequiresApi;
        import android.support.v4.app.NotificationCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.LinearLayout;
        import android.widget.Switch;
        import android.widget.Toast;
        import android.app.AlarmManager;
        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import java.util.Calendar;

        import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class AlarmActivity extends AppCompatActivity {
    Switch onOff;
    LinearLayout setting;
    Button setbtn,cancelbtn;

    // 레이아웃 컴포넌트들 받아오기
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        // 레이아웃 컴포넌트들 받아오기
        setbtn=(Button)findViewById(R.id.setbtn);
        cancelbtn=(Button)findViewById(R.id.cancelbtn);
        setting = (LinearLayout) findViewById(R.id.layoutSetting);
        onOff = (Switch) findViewById(R.id.switchAlarm);

        // 안 보이게 하기(기본설정)
        setting.setVisibility(View.INVISIBLE);
        setbtn.setVisibility(View.INVISIBLE);
        cancelbtn.setVisibility(View.INVISIBLE);

        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mLargeIconForNoti =
                        BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_foreground);

                PendingIntent mPendingIntent = PendingIntent.getActivity(AlarmActivity.this,0,
                        new Intent(getApplicationContext(),AlarmActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mBuilder=
                        new NotificationCompat.Builder(AlarmActivity.this)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("알림 제목")
                                .setContentText("알림 내용")
                                .setLargeIcon(mLargeIconForNoti)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true)
                                .setContentIntent(mPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.notify(0,mBuilder.build());
            }
        });
        // 스위치 설정
        onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onOff.isChecked()==true){
                    Toast.makeText(getApplicationContext(), "알람을 사용합니다.", Toast.LENGTH_LONG).show();
                    setting.setVisibility(View.VISIBLE);
                    setbtn.setVisibility(View.VISIBLE);
                    cancelbtn.setVisibility(View.VISIBLE);
                }
                else {
                    setting.setVisibility(View.INVISIBLE);
                    setbtn.setVisibility(View.INVISIBLE);
                    cancelbtn.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    // 취소 버튼
    public void onCancelClick(View view) {
        Intent it=new Intent(getApplication(),MainActivity.class);
        startActivity(it);
    }
}
