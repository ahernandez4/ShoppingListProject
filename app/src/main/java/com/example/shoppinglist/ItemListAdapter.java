package com.example.shoppinglist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends BaseAdapter {
    private List<ListItem> mylist = null;
    private Context context = null;
    public ItemListAdapter(Context c, List<ListItem> list){
        this.context = c;
        this.mylist = list;
    }
    @Override
    public int getCount() {
        if(mylist != null){
            return mylist.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        if(mylist!= null){
            obj = mylist.get(position);
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemListView itemlistview = null;
        if(convertView != null){//might need to change to == null and then get infale from r.layout.item_layout
            itemlistview = (ItemListView) convertView.getTag();
        }
        else{
            convertView = View.inflate(context, R.layout.item_layout, null);//
            //itemlistview = new ItemListView(convertView);
            CheckBox listitemcb = (CheckBox) convertView.findViewById(R.id.checkboxname);
            TextView listitemtext = (TextView) convertView.findViewById(R.id.itemname);
            itemlistview = new ItemListView(convertView);
            itemlistview.setItemcheckbox(listitemcb);
            itemlistview.setItemtextview(listitemtext);
            convertView.setTag(itemlistview);
        }
        //set checkbox
        //set text
        ListItem mitem = mylist.get(position);
        itemlistview.getItemcheckbox().setChecked((mitem.isChecked()));
        itemlistview.getItemtextview().setText(mitem.getItemtext());
        return convertView;
        //return null;
    }
}
