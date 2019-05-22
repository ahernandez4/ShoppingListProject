package com.example.shoppinglist;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final private static int PLUS = 1000;
    final private static int CHECK = 2000;
    final private static String PREFS_NAME = "PreferencesFile";
    private String tablename = "";
    final private static String SERVER = "http://delphi.cs.csub.edu/~ahernand/";
    List<ListItem> mlist;
    boolean backgroundtask;
    boolean wehavecheckboxesselected = false;
    FloatingActionButton fab = null;
    ListView mylistview = null;
    SwipeRefreshLayout pull2refresh;
    ItemListAdapter listAdapter;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        tablename = preferences.getString("listname",null);

        pull2refresh = findViewById(R.id.pull_refresh);
        pull2refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getListFromDB();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        fab = findViewById(R.id.fab);//
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wehavecheckboxesselected){
                    for(int i = 0; i < mlist.size(); i++){
                        if(mlist.get(i).isChecked()){
                            mlist.get(i).setStrikethrough(true);
                            mlist.get(i).setChecked(false);
                            deleteByID(mlist.get(i).getPid());
                        }
                    }
                    changeFab(PLUS);
                    listAdapter.notifyDataSetChanged();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Enter new item");
                    final EditText input = new EditText(getApplicationContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ListItem mitem = new ListItem();
                            mitem.setChecked(false);
                            String tempstr = input.getText().toString();
                            //additemtodb(tempstr,1,1);
                            //sendGet();
                            mitem.setItemtext(tempstr);
                            mlist.add(mitem);
                            additemtodb(mitem.getItemtext(),1,1);
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                builder.show();
                }
            }
        });
        //test
        mylistview = findViewById(R.id.mainlist);
        mlist = this.getInitItemList();
        mlist.clear();
        listAdapter = new ItemListAdapter(getApplicationContext(),R.layout.item_layout,mlist);

        mylistview.setAdapter(listAdapter);
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mlist.get(position).isChecked()) {
                    mlist.get(position).setChecked(false);
                    ImageView myimage = (ImageView) view.findViewById(R.id.imagev);
                    myimage.setImageResource(R.drawable.btn_circle_normal);
                    if(!check_cb()){
                        changeFab(PLUS);
                    }

                }
                else {
                    wehavecheckboxesselected = true;
                    mlist.get(position).setChecked(true);
                    ImageView myimage = (ImageView) view.findViewById(R.id.imagev);
                    myimage.setImageResource(R.drawable.btn_check_on);
                    changeFab(CHECK);
                }
            }
        });
    }

    private void getListFromDB() throws JSONException {
        //json simple request
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        String url = SERVER+"getallrows.php";
        HashMap<String, String> params2 = new HashMap<>();
        params2.put("listname",tablename);
        MyCustomJsonRequest jsonRequest = new MyCustomJsonRequest(Request.Method.POST,url, new JSONObject(params2),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    mlist.clear();
                    JSONObject[] objects = new JSONObject[response.length()];
                    for(int i = 0; i < response.length(); i++){
                        objects[i] = response.getJSONObject(i);
                        String name = objects[i].getString("name");
                        ListItem item = new ListItem();
                        item.setChecked(false);
                        item.setItemtext(name);
                        item.setPid(Integer.parseInt(objects[i].getString("pid")));
                        mlist.add(item);
                        if(i+1 == response.length()){
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error getlistfromdb",error.toString());
            }
        });
        //
        //Log.d("Error getlistfromdb",error.toString());
        //rq.add(jsonArrayRequest);
        rq.add(jsonRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, 0, 0, "Refresh");
        //if (check_cb()){
            menu.add(0, 1, 1, "Undo Delete");
        //}
        menu.add(0, 2, 2, "Show List Name");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {
            try {
                getListFromDB();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        if(id == 1){
            for(int i = 0; i < mlist.size(); i++){
                if(mlist.get(i).isStrikethrough()){
                    additemtodb(mlist.get(i).getItemtext(),1,1);
                }
            }
        }
        if(id == 2){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(tablename);
            builder.setCancelable(true);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        }

        return super.onOptionsItemSelected(item);
    }
    private void deleteByName(String s){
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        String url = SERVER+"removeitem.php";
        HashMap<String, String> params2 = new HashMap<>();
        params2.put("listname",tablename);
        params2.put("name",s);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params2), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR del Name",error.toString());
            }
        });
    }
    private void deleteByID(int id){
        String stringid = String.valueOf(id);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        String url = SERVER+"removeitem.php";
        HashMap<String, String> params2 = new HashMap<String, String>();
        params2.put("listname",tablename);
        params2.put("pid",stringid);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params2), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("In deletebyid", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR del id",error.toString());
            }
        });
        rq.add(jsonObjectRequest);
    }
    private void sendGet(){//a test to check if php responds
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        String url = "";//a php that echos a response
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Reponse", "Reponse from sendget: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(jsonObjectRequest);
    }
    private void changeFab(int TYPE){
        final int Type = TYPE;
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (Type){
                    case PLUS:
                        fab.setImageResource(R.drawable.ic_btn_add);
                        break;
                    case CHECK:
                        fab.setImageResource(R.drawable.ic_checkmark_holo_light);
                        break;
                }
            }
        });
    }
    private void additemtodb(String name, int quantity, int days){
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        String url = SERVER + "add.php";
        HashMap<String,String> params2 = new HashMap<String,String>();
        params2.put("listname",tablename);
        params2.put("name",name);
        params2.put("quantity",String.valueOf(quantity));
        params2.put("days",String.valueOf(days));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params2), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error JSON", error.toString());
            }
        });
        rq.add(jsonObjectRequest);
    }
    private List<ListItem> getInitItemList(){
        String itemTextArr[] = {"Eggs","Milk","Carrots", "Apples","Rice","Chicken"};
        List<ListItem> mlist = new ArrayList<ListItem>();
        return new ArrayList<ListItem>();
        /*
        int lenght = itemTextArr.length;
        for(int i = 0; i < lenght; i++){
            ListItem temp = new ListItem();
            temp.setChecked(false);
            temp.setItemtext(itemTextArr[i]);
            //Log.d("array list i",itemTextArr[i]);
            mlist.add(temp);
        }
        return mlist;
        */
    }
    private boolean check_cb() {

        wehavecheckboxesselected = false;
        for (int i = 0; i < mlist.size() && !wehavecheckboxesselected; i++) {
            if (mlist.get(i).isChecked()) {
                return wehavecheckboxesselected = true;
                //Log.d("checking thread", "Check to see if we are running thread");

            }
        }
        return wehavecheckboxesselected;
    }
}
