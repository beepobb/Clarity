package com.example.clarity.model.repository;

import android.graphics.Bitmap;

import com.example.clarity.model.util.ImageUtil;
import com.example.clarity.model.util.UrlUtilMethods;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;


// GenAISummary class is responsible to interact with the AWSAPI endpoint to get a summary with bitmap data only.
// Underneath the hood, the cloud compute of AWS Lambda obtains the summary via BASE64.api calls.
class GenAISummary {
    final String endPointSummary = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/summary";
    Executor executor;

    // Access limited to within repo package
    GenAISummary(Executor executor) {
        this.executor = executor;
    }

    // Request to obtain a summary text from the given poster bitmap.
    void bitmapToTextSummaryRequest(Bitmap bm, RestRepo.RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete("Updated");
                try {
                    String base64Text = ImageUtil.getStringImage(bm, Bitmap.CompressFormat.JPEG);
                    base64Text = base64Text.replace("\n", "");
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("document", base64Text);
                    String response = UrlUtilMethods.urlPost(endPointSummary, new JSONObject(data));
                    JSONObject tmp = new JSONObject(response);
                    String result = tmp.getString("summary");
                    callback.onComplete(result.substring(1,result.length() - 1));
                }
                catch(Exception e) {
                    callback.onComplete("");
                }
            }
        });
    }
}
