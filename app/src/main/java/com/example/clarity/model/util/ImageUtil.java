package com.example.clarity.model.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public static   String getStringImage(Bitmap bmp, Bitmap.CompressFormat format){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, out);
        byte[] imageBytes = out.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static Bitmap getImageFromString(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static String getFileType(String filename) {
        int index = filename.lastIndexOf('.');
        if(index > 0) {
            return filename.substring(index + 1);
        }
        return "";
    }
}
