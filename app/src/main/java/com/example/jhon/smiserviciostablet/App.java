package com.example.jhon.smiserviciostablet;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by salva on 27/07/2017.
 */

public class App extends Application{
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
