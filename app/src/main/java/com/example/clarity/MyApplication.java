package com.example.clarity;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    /*
    Custom Application class for maintaining global application state.
    Will instantiate when application first starts (is a singleton class).
    Things initialized here remain for entire application life cycle.
    Add [android:name=".MyApplication"] to the AndroidManifest
     */

    // Initialize Threads and Database on application start.
    // Multithreading based on the Executor and Runnable Interface.
    private Executor executor = Executors.newFixedThreadPool(8);
    private RestRepo database = RestRepo.getInstance(executor);

    private User appUser; // User object for logged in user

    // Database methods //
    public RestRepo getDatabase() {
        return database;
    }
    public Executor getExecutor() { return executor; }

    // Logged-in user methods //
    public User getAppUser() { return appUser; }
    public void saveAppUser(User user) {
        appUser = user;
    }

}
