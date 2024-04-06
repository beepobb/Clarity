package com.example.clarity;

import android.app.Application;

import com.example.clarity.model.repository.RestRepo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    /*
    Custom Application class for maintaining global application state.
    Will instantiate when application first starts.
    Things initialized here remain for entire application life cycle.
    Add [android:name=".MyApplication"] to the AndroidManifest
     */

    // Initialize Threads and Database on application start
    private final Executor executor = Executors.newFixedThreadPool(2);
    private final RestRepo database = RestRepo.getInstance(executor);

    public RestRepo getDatabase() {
        return database;
    }
}
