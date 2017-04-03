package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by cking10 on 2/17/2017.
 */

class ArticleLoader<Article> extends AsyncTaskLoader<List<Article>> {
    private String mUri;

    ArticleLoader(Context context, String uri){
        super(context);
        mUri = uri;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        List<Article> articles = null;

        if (mUri != null) {
            try {
                articles  = (List<Article>) QueryUtils.fetchArticleData(mUri);
            } catch (Exception e) {
                Log.e("ArticleLoader", "Cast to List<Article> failed");
            }
        }

        return articles;

    }
}
