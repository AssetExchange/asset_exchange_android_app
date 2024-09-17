package com.example.assetexdemo1;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class AssetExchangeApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Force light mode for the entire app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
