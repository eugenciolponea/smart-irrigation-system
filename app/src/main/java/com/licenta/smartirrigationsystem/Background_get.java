package com.licenta.smartirrigationsystem;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Background_get extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            /* Change the IP to the IP you set in the raspberry sketch */
            URL url = new URL("http://192.168.1.7/?" + params[0]); // change raspberry pi ip
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine).append("\n");

            in.close();
            connection.disconnect();
            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}