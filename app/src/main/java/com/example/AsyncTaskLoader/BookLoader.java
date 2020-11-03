package com.example.AsyncTaskLoader;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class BookLoader extends AsyncTaskLoader<String> {

    private String query;

    public BookLoader(Context context, String str) {
        super(context);
        this.query = str;
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(query);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}
