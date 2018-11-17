package com.mobile.donut;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class SetActivity extends AppCompatActivity {
    final Calendar cal = Calendar.getInstance();
    ImageButton startDate;
    ImageButton endDate;
    TextView startD;
    TextView endD;

    protected void onCreate(Bundle savedInstanceState) {
        final String[] data=getResources().getStringArray(R.array.성명);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);



        startDate = (ImageButton)findViewById(R.id.imageButton);
        endDate = (ImageButton)findViewById(R.id.imageButton2);
        startD = (TextView) findViewById(R.id.editText2);
        endD = (TextView) findViewById(R.id.editText3);

        // 시작달력띄우기
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate();
            }
        });

        // 끝달력띄우기
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate();
            }
        });

        Spinner spinner=(Spinner)findViewById(R.id.spinner2);
        Spinner spinner2=(Spinner)findViewById(R.id.spinner3);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
        spinner.setAdapter(adapter);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
        spinner2.setAdapter(adapter2);
    }
    public void ButtonClick(View view) {
        Intent intent=new Intent(getApplicationContext(),SavingActivity.class);
        startActivity(intent);
    }

    public void startDate(){
        // 날짜

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String msg = String.format("%d 년 %d 월 %d 일", year, month+1, dayOfMonth);
                Toast.makeText(SetActivity.this, msg, Toast.LENGTH_SHORT).show();
                startD.setText(msg);

            }
        },  cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        // dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
        dialog.show();
    }


    public void endDate(){
        // 날짜

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String msg = String.format("%d 년 %d 월 %d 일", year, month+1, dayOfMonth);
                Toast.makeText(SetActivity.this, msg, Toast.LENGTH_SHORT).show();
                endD.setText(msg);

            }
        },  cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        // dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
        dialog.show();
    }

}

