package com.example.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class MainMenuActivity extends AppCompatActivity {
    private String tablename = "";
    SharedPreferences.Editor prefEditor;
    //SharedPreferences preferences;
    final private static String PREFS_NAME = "PreferencesFile";
    final private static String MYASCII = "1234567890abcdefghijklmnopqrstuvwxyz";
    final private static String SERVER = "http://delphi.cs.csub.edu/~ahernand/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        prefEditor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        //preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        tablename = makelistname();
        Button startbtn = findViewById(R.id.makenew);
        final Button importbtn = findViewById(R.id.importlist);
        Button uelabtn = findViewById(R.id.button3);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makelistindb();
                //tablename = "110510";//works
                //checktableindb();
            }
        });
        importbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                builder.setTitle("Enter list name");
                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tablename = input.getText().toString();
                        
                        checktableindb();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        uelabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private String makelistname(){
        boolean success = false;
        String uniquestring = "abcdefzqy21";
        //uniquestring = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //if(uniquestring.length() > 5){
        //    success = true;
        //}
        if(!success) {
            uniquestring = String.valueOf( Build.TAGS.length()%10 + Build.BOARD.length()%10+
                    Build.BRAND.length()%10 + Build.HOST.length()%10 +
                    Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                    Build.DISPLAY.length()%10 + Build.ID.length()%10 +
                    Build.MANUFACTURER.length()%10 + Build.MODEL.length()%10 +
                    Build.PRODUCT.length()%10 + Build.TYPE.length()%10 +
                    Build.USER.length()%10);
            success = (uniquestring.length() > 9)? true : false;
        }
        if(!success){
            Date d = new Date();
            SimpleDateFormat vformat = new SimpleDateFormat("ddMMyyyy");
            uniquestring = vformat.format(d) + make4char();
        }
        //uniquestring = uniquestring.substring(3);
        
        return uniquestring;
    }
    private String make4char(){
        Random r = new Random();
        char[] c = new char[4];
        for(int i = 0; i < 4; i++){
            c[i] = MYASCII.charAt(r.nextInt(MYASCII.length()));
        }
        return new String(c);
    }
    private void makelistindb(){
        RequestQueue rq = Volley.newRequestQueue(MainMenuActivity.this);
        String url = SERVER+"maketable.php";
        HashMap<String, String> params2 = new HashMap<>();
        tablename = makelistname();//
        params2.put("name",tablename);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params2), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                
                checktableindb();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR del Name",error.toString());
            }
        });
        rq.add(jsonObjectRequest);
    }
    private void checktableindb(){
        RequestQueue rq = Volley.newRequestQueue(MainMenuActivity.this);
        String url = SERVER+"checktable.php";
        HashMap<String, String> params2 = new HashMap<>();
        params2.put("name",tablename);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params2), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //
                //
                String s = response.toString();
                if(s.contains("true")){
                    prefEditor.putString("listname",tablename);//the table exists let's add it to our prefs
                    //prefEditor.apply();
                    prefEditor.putBoolean("usermadealist",true);
                    prefEditor.commit();
                    Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR del Name",error.toString());
            }
        });
        rq.add(jsonObjectRequest);
    }
}
