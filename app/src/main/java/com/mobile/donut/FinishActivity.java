package com.mobile.donut;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
    }

    public void onButtonMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 메인메뉴로 넘어감
        startActivity(intent);
    }
}
