//Adapted from Dhruv Raval -- http://stackoverflow.com/questions/8992964/android-load-from-url-to-bitmap
package com.example.android.newsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cking10 on 2/15/2017.
 */

class ImageFileTask extends AsyncTask<URL, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(URL... params) {
        try {
            if (params[0] != null) {
                HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } else {
                return null;
            }
        } catch (IOException e) {
            Log.e("ImageFileTask", "Failed to retrieve image bitmap");
            return null;
        }
    }
}
