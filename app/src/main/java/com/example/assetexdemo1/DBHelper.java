package com.example.assetexdemo1;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assetexdemo1.model.LoadsModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DBHelper {
    private static final String URL = "http://192.168.1.3/demo/fetch_data.php";

    public static void fetchData(Context context, final OnDataReceivedListener listener) {
        StringRequest request = new CachingStringRequest(Request.Method.GET, URL, new Response.Listener <String> () {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<LoadsModel> dataList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            System.out.println(jsonObject.toString());

                            LoadsModel loadsModel = new LoadsModel(
                                    jsonObject.getString("load_name"),
                                    jsonObject.getString("client_name"),
                                    String.valueOf(jsonObject.getInt("load_status")),
                                    LocalDateTime.parse(jsonObject.getString("load_date"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                    LocalDateTime.parse(jsonObject.getString("load_due"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"))
                            );

                            dataList.add(loadsModel);
                        }

                        listener.onDataReceived(context, dataList);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    e.printStackTrace();
                }
            }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static void updateData(Context context, LoadsModel model) {
        StringRequest request = new CachingStringRequest(Request.Method.POST, URL, new Response.Listener<String> () {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("load_name", String.valueOf(model.getLoadName()));
                params.put("client_name", model.getLoadClient());
                params.put("load_status", String.valueOf(model.getLoadStatus().length()));
                params.put("load_date", model.getLoadDate().toString());
                params.put("load_due", model.getLoadDue().toString());

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }


    public interface OnDataReceivedListener {
        void onDataReceived(Context context, List<LoadsModel> dataList);
    }
}
