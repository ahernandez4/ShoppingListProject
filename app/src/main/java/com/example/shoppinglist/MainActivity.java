package com.example.shoppinglist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<ListItem> mlist;
    boolean wehavecheckboxesselected = false;
    FloatingActionButton fab = null;
    ListView mylistview = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);//
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //test
        mylistview = findViewById(R.id.mainlist);
        mlist = this.getInitItemList();
        //Log.d("mlist contians",mlist.get(1).getItemtext());
        final ItemListAdapter listAdapter = new ItemListAdapter(getApplicationContext(),mlist);

        mylistview.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("CLICK","We clicked an item");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void sendGet(){//maybe pass a list or maybe context or maybe...

    }

    //test
    private List<ListItem> getInitItemList(){
        String itemTextArr[] = {"Eggs","Milk","Vegetables", "Apples","Rice","Chicken"};
        List<ListItem> mlist = new ArrayList<ListItem>();
        int lenght = itemTextArr.length;
        for(int i = 0; i < lenght; i++){
            ListItem temp = new ListItem();
            temp.setChecked(false);
            temp.setItemtext(itemTextArr[i]);
            //Log.d("array list i",itemTextArr[i]);
            mlist.add(temp);
        }
        return mlist;
    }
    private void check_cb(){
        Log.d("before thread","Check to see if we are in check_cb");
        wehavecheckboxesselected = false;
        class mychecker implements Runnable{
            @Override
            public void run() {
                boolean foundselected = false;
                for(int i = 0; i < mlist.size() && !foundselected; i++){
                    if(mlist.get(i).isChecked()){
                        foundselected = true;
                        wehavecheckboxesselected = true;
                        Log.d("checking thread","Check to see if we are running thread");

                    }
                }
                if(wehavecheckboxesselected){
                    fab.setImageResource(R.drawable.ic_checkmark_holo_light);
                }
                else{
                    fab.setImageResource(R.drawable.ic_btn_add);
                }
            }
        }
        new Thread(new mychecker()).start();
    }
}
