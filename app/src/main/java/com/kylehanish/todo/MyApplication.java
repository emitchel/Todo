package com.kylehanish.todo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class MyApplication extends Application {


    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

}
