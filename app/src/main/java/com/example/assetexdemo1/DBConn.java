package com.example.assetexdemo1;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBConn {
    public static String connectionHost = "https://beige-snake-192211.hostingersite.com/"; // "http://192.168.1.9/demo/"; // "https://beige-snake-192211.hostingersite.com/"; // "http://192.168.1.9/demo/"; // 192.168.43.72 // "http:/172.20.10.12/demo/"; // 192.168.43.72

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
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static void postRequest(String url, Context context, Map<String, String> params, ResponseCallback responseCallback, String onErrorString, String onErrorString2) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static void putRequest(String url, Context context, JSONObject params, ResponseCallback responseCallback, String onErrorString, String onErrorString2) {
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
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
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return params.toString() == null ? new byte[0] : params.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported encoding");
                    return null;
                }
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static abstract class ResponseCallback {
        public void onResponse(String response, Context context, String onErrorString) {
            try {
                if (response.equals("")) {
                    System.out.println("Response is empty");
                }
                else {
                    System.out.println(response);
                    Object json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        if (!((JSONObject) json).isNull("code")) {
                            if (((JSONObject) json).getString("code").startsWith("Error")) {
                                throw new Exception(((JSONObject) json).getString("code"));
                            }
                            else {
                                List<String> jsonKeysList = new ArrayList<>();
                                ((JSONObject) json).keys().forEachRemaining(jsonKeysList::add);
                                jsonKeysList.remove("code");

                                if (!jsonKeysList.isEmpty()) {
                                    this.innerResponse(json, context);
                                }
                                else {
                                    this.innerResponse(((JSONObject) json).getString("code"), context);
                                }
                            }
                        }
                        else if (!((JSONObject) json).isNull("records")) {
                            JSONArray jsonArray = new JSONArray(((JSONObject) json).getString("records"));
                            this.innerResponse(jsonArray, context);
                        }
                        else {
                            this.innerResponse(json, context);
                        }
                    }
                    else if (json instanceof JSONArray) {
                        this.innerResponse(json, context);
                    }
                    else {
                        this.innerResponse(json);
                    }
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
        public void innerResponse(Object object, Context context) {
            innerResponse(object);
        }
    }
}
