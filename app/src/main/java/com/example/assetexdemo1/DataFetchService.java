package com.example.assetexdemo1;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.android.volley.VolleyError;
import org.json.JSONArray;

public class DataFetchService extends Service {
    private static final int FETCH_INTERVAL = 30 * 1000;
    private final Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        startFetchingData();
    }

    private void startFetchingData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // DBConn.getRequest();
                handler.postDelayed(this, FETCH_INTERVAL);
            }
        }, FETCH_INTERVAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // We don't need to bind this service
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Stop fetching when service is destroyed
    }
}
