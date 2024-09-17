package com.example.assetexdemo1;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DBConn {
    public static String connectionHost = "http://192.168.1.3/demo/";

    public static String getURL(String path) {
        return connectionHost + path;
    }

    public static String getRecordURL(String path) {
        return connectionHost + "api.php/records/" + path;
    }

    public static void getRequest(String url, Context context, ResponseCallback responseCallback, String onErrorString, String onErrorString2) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (responseCallback != null) {
                    responseCallback.onResponse(response, context, onErrorString2);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, onErrorString, Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static abstract class ResponseCallback {
        public void onResponse(String response, Context context, String onErrorString) {
            try {
                System.out.println(response);
                Object json = new JSONTokener(response).nextValue();
                if (json instanceof JSONObject) {
                    if (!((JSONObject) json).isNull("code")) {
                        throw new Exception(((JSONObject) json).getString("code"));
                    }
                    else if (!((JSONObject) json).isNull("records")) {
                        JSONArray jsonArray = new JSONArray(((JSONObject) json).getString("records"));
                        this.innerResponse(jsonArray);
                    }
                }
                else if (json instanceof JSONArray) {
                    this.innerResponse(json);
                }
            }
            catch (Exception e) {
                if (e.getMessage() != null && !e.getMessage().equals("")) {
                    Toast.makeText(context, onErrorString + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                else if (onErrorString != null && !onErrorString.equals("")) {
                    Toast.makeText(context, onErrorString, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "Unknown Error", Toast.LENGTH_LONG).show();
                }
                e.printStackTrace();
            }
        }

        public abstract void innerResponse(Object object);
    }
}
