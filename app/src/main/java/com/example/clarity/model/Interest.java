package com.example.clarity.model;

import android.graphics.drawable.Drawable;

public class Interest {
    private String name;
    private Drawable drawable;

    public Interest(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public Drawable getDrawable(){return drawable;}
}
