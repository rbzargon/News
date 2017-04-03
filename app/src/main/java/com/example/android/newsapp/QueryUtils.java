//Adapted from ud843-QuakeReport QueryUtils.java
package com.example.android.newsapp;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cking10 on 2/11/2017.
 */

final class QueryUtils {

    private static final String LOG_TAG = "QueryUtils" ;

    private QueryUtils() {
        throw new AssertionError("QueryUtils instantiation not allowed");
    }

    static List<Article> fetchArticleData(String requestUri){
        URL url = createUrl(Uri.parse(requestUri));

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return extractArticlesFromJSON(jsonResponse);
    }

    //adapted from Udacity Earthquake app
    private static List<Article> extractArticlesFromJSON(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Article> articles = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        //set the value for last page
        int lastPage = 0;
        try {
            lastPage = baseJsonResponse.getJSONObject("response").getInt("pages");
            MainActivity.setPageLast(lastPage);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        JSONArray articleArray = null;
        try {
            articleArray = baseJsonResponse.getJSONObject("response").getJSONArray("results");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        // For each Article in the articleArray, parse relevant info and add to Articles list
        for (int i = 0; i < articleArray.length(); i++) {

            JSONObject currentArticle = null;
            try {
                currentArticle = articleArray.getJSONObject(i);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            String section = null;
            try {
                section = currentArticle.getString("sectionName");
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            String title = null;
            try {
                title = currentArticle.getString("webTitle");
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            String pubDate = null;
            try {
                pubDate = currentArticle.getString("webPublicationDate");
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            URL imgURL = null;
            try {
                currentArticle.getJSONObject("fields").getString("thumbnail");
                imgURL = createUrl(Uri.parse(currentArticle.getJSONObject("fields").getString("thumbnail")));
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            Uri linkUri = null;
            try {
                linkUri = Uri.parse(currentArticle.getString("webUrl"));
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            // Add the new {@link Article} to the list of articles
            articles.add(new Article(section, title, pubDate, imgURL, linkUri));
        }
        return articles;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        int connResponse = urlConnection.getResponseCode();
        if(connResponse == HttpsURLConnection.HTTP_OK){
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static URL createUrl(Uri uri){
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
