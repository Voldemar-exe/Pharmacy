package com.example.pharmacy;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(BuildConfig.API_KEY);
    }
}
