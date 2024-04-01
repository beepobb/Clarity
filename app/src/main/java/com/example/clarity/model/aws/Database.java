package com.example.clarity.model.aws;
import com.example.clarity.model.data.*;

import java.net.URL;
import java.net.HttpURLConnection;

import java.net.*;
import java.io.*;


public class Database {
    static String endPoint = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/user";
    public static String urlGet(String targetURL, String urlParameters) throws Exception{
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static User[] getUser(int id, int start, int end) {
        try {
            String response = urlGet("https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/user", "");
            System.out.println(response);
        }
        catch(Exception e) {
            return null;
        }
        return null;
    }
}
