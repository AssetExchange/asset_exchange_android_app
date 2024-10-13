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
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                "ASSET_EXCHANGE_NOTIFICATIONS",
                "Asset Exchange",
                NotificationManager.IMPORTANCE_DEFAULT
            );

            serviceChannel.setDescription("Asset Exchange Notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
