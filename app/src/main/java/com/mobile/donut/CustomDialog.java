package com.mobile.donut;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    final Calendar calender = Calendar.getInstance();
    private String txtMonth;
    private String txtYear;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore db;
    private String connectCode;

    private EditText date;
    private EditText content;
    private EditText price;
    private Spinner itemIndex;

    private TextView Cancel;
    private TextView Input;

    private String name;

    private ListActivity.ListAdapter listAdapter;
    private ListItem listItem;

    private int indexStart;
    private int indexEnd;
    private ImageButton datePicker;

    public CustomDialog(Context context, String name, ListActivity.ListAdapter listAdapter){
        super(context);
        this.context = context;
        this.name = name;
        this.listAdapter = listAdapter;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        connectCode = ListActivity.getConnectID();

        date = (EditText) findViewById(R.id.editDate);
        price = (EditText) findViewById(R.id.editPrice);
        content = (EditText) findViewById(R.id.editContent);
        itemIndex = (Spinner)findViewById(R.id.Item);
        datePicker = (ImageButton)findViewById(R.id.calender);
        txtMonth = null;
        txtYear = null;

        // 데이트피커로 날짜 받기
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        // 어댑터로 항목 받아오기
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.항목,R.layout.support_simple_spinner_dropdown_item);

        // 스피너에 어댑터 붙이기
        itemIndex.setAdapter(adapter);

        Cancel = (TextView) findViewById(R.id.btnIn);
        Input = (TextView) findViewById(R.id.btnCancel);

        Cancel.setOnClickListener(this);
        Input.setOnClickListener(this);

        if(!TextUtils.isEmpty(name)){
            date.setText(name);
        }

        db.collection(connectCode).document("index").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                indexStart = Integer.parseInt(documentSnapshot.getString("indexStart"));
                indexEnd = Integer.parseInt(documentSnapshot.getString("indexEnd"));
            }
        });
    }

    public EditText getDate() {
        return date;
    }

    public void setDate(EditText date) {
        this.date = date;
    }

    public EditText getStory() {
        return content;
    }

    public void setStory(EditText content) {
        this.content = content;
    }

    public EditText getPrice() {
        return price;
    }

    public void setCost(EditText cost) {
        this.price = price;
    }

    @Override
    public void onClick(View v) {
        int total;

        switch (v.getId()){
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnIn:
                String date =  this.date.getText().toString();
                String item =  itemIndex.getSelectedItem().toString();
                String content =  this.content.getText().toString();
                String price =  this.price.getText().toString();

                listAdapter.addItem(new ListItem(item, date, content, price, "갱신 전"));

                HashMap<String, Object> newItem = new HashMap<>();
                newItem.put("date", date);
                newItem.put("item", item);
                newItem.put("content", content);
                newItem.put("price", price);
                newItem.put("month", txtMonth); // 월별 통계를 위한 필드
                newItem.put("year", txtYear);
                newItem.put("data", true); // 후에 이 값으로 데이터 쿼리

                CollectionReference colRef = db.collection(connectCode);
                colRef.document(Integer.toString(indexEnd+1)).set(newItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "추가되었습니다", Toast.LENGTH_LONG).show();
                        if(indexStart==0)
                            db.collection(connectCode).document("index").update("indexStart", Integer.toString(indexStart+1));
                        db.collection(connectCode).document("index").update("indexEnd", Integer.toString(indexEnd+1));
                    }
                });

                dismiss();
                break;
        }
    }

    public void DateDialog(){
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String msg = String.format("%d.%d.%d", year, month+1, dayOfMonth);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                date.setText(msg);

                txtMonth = Integer.toString(month+1);
                txtYear = Integer.toString(year);
            }
        },  calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE));
        dialog.show();
    }
}
