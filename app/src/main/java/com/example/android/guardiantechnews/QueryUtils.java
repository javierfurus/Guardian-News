package com.example.android.guardiantechnews;

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

/**
 * Helper methods related to requesting and receiving newsfeed data from theguardian
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<LatestNews> fetchLatestNews(String requestURL) {
        //Create URL
        URL url = createUrl(requestURL);

        String jsonResponse = null;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        ArrayList<LatestNews> news = extractNews(jsonResponse);

        return news;
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
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unfortunately there was a problem retrieving theGuardian news JSON results.", e);
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
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating URL", e);
        }
        return url;
    }

    /**
     * Return a list of {@link LatestNews} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<LatestNews> extractNews(String theguardianJSON) {
        if (TextUtils.isEmpty(theguardianJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding latestNews to
        ArrayList<LatestNews> latestNews = new ArrayList<>();

        try {

            // build up a list of LatestNews objects with the corresponding data.
            JSONObject jsonresponse = new JSONObject(theguardianJSON);
            JSONArray featuresArray = jsonresponse.getJSONObject("response").getJSONArray("results");
            for (int i = 0; i < featuresArray.length(); i++) {
                JSONObject currentNews = featuresArray.getJSONObject(i);
                JSONArray tagsArray = currentNews.getJSONArray("tags");
                JSONObject fields = currentNews.getJSONObject("fields");
                // Extract the value for the key called "mag"
                String thumbnail = fields.getString("thumbnail");
                String place = currentNews.getString("webTitle");
                String time = currentNews.getString("webPublicationDate");
                String locationURL = currentNews.getString("webUrl");
                String section = currentNews.getString("sectionName");
                String author = null;
                for (int k = 0; k < tagsArray.length(); k++) {
                    JSONObject authors = tagsArray.getJSONObject(k);
                    author = authors.getString("webTitle");
                }


                LatestNews news = new LatestNews(time, place, thumbnail, locationURL, author, section);
                latestNews.add(news);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the theguardian JSON results", e);
        }

        // Return the list of latestNews
        return latestNews;
    }
}
