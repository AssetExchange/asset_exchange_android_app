package com.example.assetexdemo1;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class AssetExchangeApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        // Force light mode for the entire app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

//    private void createNotificationChannel() {
//        NotificationChannel serviceChannel = new NotificationChannel(
//                "ASSET_EXCHANGE_NOTIFICATIONS",
//                "Asset Exchange",
//                NotificationManager.IMPORTANCE_DEFAULT
//        );
//
//        NotificationManager manager = getSystemService(NotificationManager.class);
//        manager.createNotificationChannel(serviceChannel);
//    }
}
