package com.example.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ItemListView extends RecyclerView.ViewHolder {
    private CheckBox itemcheckbox;
    private TextView itemtextview;
    public ItemListView(@NonNull View itemView) {
        super(itemView);
    }
    public CheckBox getItemcheckbox(){
        return itemcheckbox;
    }
    public void setItemcheckbox(CheckBox itemcheckbox){
        this.itemcheckbox = itemcheckbox;
    }
    public TextView getItemtextview(){
        return itemtextview;
    }
    public void setItemtextview(TextView itemtextview){
        this.itemtextview = itemtextview;
    }
}
