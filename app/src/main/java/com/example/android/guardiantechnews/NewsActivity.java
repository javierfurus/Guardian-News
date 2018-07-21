/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.guardiantechnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<LatestNews>> {

    public static final String LOG_TAG = NewsActivity.class.getName();
    private static final int LOADER_ID = 0;
    private static final String GUARDIAN_URL = "http://content.guardianapis.com/search";
    Context context = this;
    LoaderManager loaderManager = getLoaderManager();
    private NewsAdapter mAdapter;
    private TextView emptyView;
    private ProgressBar progressBar;
    private ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        emptyView = (TextView) findViewById(R.id.emptyview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            emptyView.setText(R.string.nointernet);
            progressBar.setVisibility(View.INVISIBLE);
        }


        mAdapter = new NewsAdapter
                (NewsActivity.this, new ArrayList<LatestNews>());

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);
        newsListView.setEmptyView(emptyView);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the
        // user interface
        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position,
                                    long arg3) {
                LatestNews currentLatestNews = mAdapter.getItem(position);
                String url = currentLatestNews.getURLGuardian();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

        });
    }

    @Override
    public Loader<List<LatestNews>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String numberOfNews = sharedPrefs.getString(
                getString(R.string.settings_min_number_of_news_key),
                getString(R.string.settings_min_number_of_news_default_value));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        String topic = sharedPrefs.getString(
                getString(R.string.settings_topic_key),
                getString(R.string.settings_topic_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("page-size", numberOfNews);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("section", topic);
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "2ff9a169-1cb4-4cef-8b37-754e3ed1607e");

        // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
        return new NewsLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<LatestNews>> loader, List<LatestNews> latestNews) {
        mAdapter.clear();
        if (latestNews != null && !latestNews.isEmpty()) {
            mAdapter.addAll(latestNews);
        }
        emptyView.setText(R.string.nothingfound);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<LatestNews>> loader) {
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
