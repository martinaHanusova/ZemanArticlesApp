package com.example.android.newsapp;

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

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Article> extractFeatureFromJson(String articlesJson) {
        if (TextUtils.isEmpty(articlesJson)) {
            return null;
        }

        ArrayList<Article> articles = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(articlesJson);
            JSONArray jsonArray = jsonObject.optJSONArray("articles");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject article = jsonArray.optJSONObject(i);
                articles.add(new Article(article.optString("title"), article.optString("description"), article.optString("url"), article.optString("urlToImage"), article.optString("publishedAt")));
                Log.d(LOG_TAG, articles.get(i).toString());
            }
            return articles;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with parsing JSON", e);
        }
        return null;
    }

    public static ArrayList<Article> fetchArticleData(String requestUrl) {
        URL url = createURL(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input Stream", e);
        }
        return extractFeatureFromJson(jsonResponse);
    }
}
