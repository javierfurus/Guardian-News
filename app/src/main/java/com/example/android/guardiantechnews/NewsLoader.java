package com.example.android.guardiantechnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<LatestNews>> {
    public static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrls;

    public NewsLoader(Context context, String urls) {
        super(context);
        mUrls = urls;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<LatestNews> loadInBackground() {
        if (mUrls == null) {
            return null;
        }
        List<LatestNews> latestNews = new ArrayList<>();
        latestNews = QueryUtils.fetchLatestNews(mUrls);
        return latestNews;
    }

}
