package com.example.clarity.model.repository;

import android.graphics.Bitmap;

import com.example.clarity.model.data.Author;
import com.example.clarity.model.util.ImageUtil;
import com.example.clarity.model.util.UrlUtilMethods;

import java.util.concurrent.Executor;


class ImageRepo {
    final static String imageBucket = "https://18ihlowsu4.execute-api.ap-southeast-2.amazonaws.com/beta/sutdclarity";
    private Executor executor;

    // Access limited to within repo package
    ImageRepo(Executor executor) {
        this.executor = executor;
    }

    // Request to add Image into data base.
    void postImageRequest(Bitmap bmp, String filename, RestRepo.RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = addImage(bmp, filename);
                callback.onComplete(response);
            }
        });
    }

    // Method to add Image into data base.
    String addImage(Bitmap bmp, String filename) {
        try {
            String fileType = ImageUtil.getFileType(filename);
            Bitmap.CompressFormat format;
            if(fileType.equals("png")) {
                format = Bitmap.CompressFormat.PNG;
            }
            else if(fileType.equals("jpeg")) {
                format = Bitmap.CompressFormat.JPEG;
            }
            else{
                return "";
            }
            String image_data = ImageUtil.getStringImage(bmp, format);
            return UrlUtilMethods.urlPutImage(imageBucket+"/" +filename ,image_data);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    // Request to get Image from database.
    void getImageRequest(String url, RestRepo.RepositoryCallback<Bitmap> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(getImage(url));
            }
        });
    }

    // Method to get Image from database.
    Bitmap getImage(String url) {
        try {
            String encodedImage = UrlUtilMethods.urlGetImage(url);
            return ImageUtil.getImageFromString(encodedImage);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
