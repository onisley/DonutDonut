package com.mobile.donut;

public class ListItem {
    String item;
    String date;
    String content;
    String price;
    String total;
    String tag;

    public ListItem(String item, String date, String content, String price, String total) {
        this.item = item;
        this.date = date;
        this.content = content;
        this.price = price;
        this.total = total;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTag() {
        if(tag!=null)
            return tag;
        return "noTag";
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
