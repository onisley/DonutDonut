package com.mobile.donut;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

public class SavingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);
    }

    public void onTest(View view) {
        Intent intent=new Intent(getApplicationContext(),RequestActivity.class);
        startActivity(intent);
    }

    public void onTest2(View view) {
        Intent intent=new Intent(getApplicationContext(),FinishActivity.class);
        startActivity(intent);
    }
}

