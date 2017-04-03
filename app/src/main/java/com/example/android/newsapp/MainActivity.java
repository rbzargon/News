//Loaders adapted from ud843 - QuakeReport
package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {
    private final String BASE_QUERY = "https://content.guardianapis.com/technology?api-key=test&page=";
    private final String THUMBNAIL_QUERY = "&show-fields=thumbnail";
    private final int ARTICLE_LOADER_ID = 1;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private InputMethodManager imm;
    private static int pageNum = 1;
    private static int pageLast;
    private String search;
    private String last_search;
    private String query;
    private TextView pageTextView;

    public static void setPageLast(int pageLast) {
        MainActivity.pageLast = pageLast;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button backButton = (Button) findViewById(R.id.back_button);
        final Button nextButton = (Button) findViewById(R.id.next_button);
        pageTextView = (TextView) findViewById(R.id.page_text_view);
        query = BASE_QUERY + String.valueOf(pageNum) + THUMBNAIL_QUERY;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(null, R.string.network_check, Toast.LENGTH_LONG).show();
                } else if (pageNum != 1) {
                    pageNum--;
                    query = BASE_QUERY + String.valueOf(pageNum) + THUMBNAIL_QUERY;
                    getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null,
                            com.example.android.newsapp.MainActivity.this);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(null, R.string.network_check, Toast.LENGTH_LONG).show();
                } else if (pageLast > pageNum) {
                    pageNum++;
                    query = BASE_QUERY + String.valueOf(pageNum) + THUMBNAIL_QUERY;
                    getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null,
                            com.example.android.newsapp.MainActivity.this);
                }
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader<Article>(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        mAdapter = new ArticleAdapter(this, data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pageTextView.setText(String.valueOf(pageNum) + "/" + String.valueOf(pageLast));
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mRecyclerView.getRecycledViewPool().clear();
    }

    // http://stackoverflow.com/questions/9570237/android-check-internet-connection
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
