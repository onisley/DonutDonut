package com.mobile.donut;


        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

public class RequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
    }

    public void onClickedd(View view) {
        Intent intent=new Intent(getApplicationContext(),SavingActivity.class);
        startActivity(intent);
    }
}
