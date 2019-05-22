package com.example.shoppinglist;

import android.content.Context;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends ArrayAdapter<ListItem> {
    private int layoutresource;
    private Context ctx;
    private List<ListItem> mylist = null;

    public ItemListAdapter(Context context, int resource, List<ListItem> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.mylist = objects;
        this.layoutresource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemListView itemlistview = null;
        if(convertView == null){
            convertView = LayoutInflater.from(ctx).inflate(layoutresource,parent,false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imagev);
        TextView listitemtext = (TextView) convertView.findViewById(R.id.itemname);
        itemlistview = new ItemListView(convertView);
        itemlistview.setItemtextview(listitemtext);
        ListItem mitem = mylist.get(position);
        if(mitem.isChecked()){
            imageView.setImageResource(R.drawable.btn_check_on);

        }else{
            imageView.setImageResource(R.drawable.btn_circle_normal);

        }
        if(mitem.isStrikethrough()){
            listitemtext.setPaintFlags(listitemtext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            listitemtext.setPaintFlags(0);
        }
        itemlistview.getItemtextview().setText(mitem.getItemtext());
        return convertView;

    }
}
