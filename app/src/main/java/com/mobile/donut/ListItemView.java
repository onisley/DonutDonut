package com.mobile.donut;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListItemView extends LinearLayout {
    TextView item;
    TextView date;
    TextView content;
    TextView price;
    TextView total;

    public ListItemView(Context context) {
        super(context);
        init(context);
    }

    public ListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        // food_item.xml을 대상으로 inflation 하는 코드 작성
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item,this,true);

        item = (TextView)findViewById(R.id.txtItem);
        date = (TextView)findViewById(R.id.txtDate);
        content = (TextView)findViewById(R.id.txtContent);
        price = (TextView)findViewById(R.id.txtPrice);
        total = (TextView)findViewById(R.id.txtTotal);
    }

    public void setItem(String string){
        item.setText(string);
    }

    public void setDate(String string) {
        date.setText(string);
    }

    public void setContent(String string) {
        content.setText(string);
    }

    public void setPrice(String string) {
        price.setText(string);
    }

    public void setTotal(String string) {
        total.setText(string);
    }
}
