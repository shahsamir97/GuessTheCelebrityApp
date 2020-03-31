package com.example.guessthecelebrityapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadDataTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        URL url = null;
        String webContent = "";
        try {
            url = new URL(urls[0]);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();

            while (data != -1) {
                char currentCharacter = (char) data;
                webContent += currentCharacter;
                data = reader.read();
            }
            Log.i("URL", urls[0]);
            return webContent;

        } catch (IOException e) {
            e.printStackTrace();
            return "Failed";
        }
    }
}
