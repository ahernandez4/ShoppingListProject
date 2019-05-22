package com.example.shoppinglist;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class MyCustomJsonRequest<T> extends JsonRequest<JSONArray> {
    private JSONObject mRequestObj;
    private Response.Listener<JSONArray> mReponseListener;

    public MyCustomJsonRequest(int method, String url, JSONObject requestObj, Response.Listener<JSONArray> responseLister, Response.ErrorListener errorListener){
        super(method, url, (requestObj == null)? null : requestObj.toString(), responseLister, errorListener);
        this.mRequestObj = requestObj;
        this.mReponseListener = responseLister;
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        super.deliverResponse(response);
        mReponseListener.onResponse(response);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            try {
                return Response.success(new JSONArray(json),HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e) {
                return Response.error(new ParseError(e));
            }
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}
