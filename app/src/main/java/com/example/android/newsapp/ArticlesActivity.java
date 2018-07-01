package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = ArticlesActivity.class.getSimpleName();
    private static final String URL_REQUEST = "https://newsapi.org/v2/everything?apiKey=7aad192673be4f0b80f1fcae4d448ee5&q=zeman&from=2018-02-22&sortBy=relevancy&language=cs";
    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleAdapter articleAdapter;
    private ProgressBar progressBar;
    private TextView messageView;
    private ImageView backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        ListView listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.loading);
        messageView = findViewById(R.id.message_view);
        backgroundImage = findViewById(R.id.zeman_image);
        listView.setEmptyView(messageView);
        backgroundImage.setVisibility(View.GONE);

        articleAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(articleAdapter);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            messageView.setText("No internet, Pussy");
            backgroundImage.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(articleAdapter.getItem(position).getUrl()));
                startActivity(intent);
            }
        });


    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, URL_REQUEST);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        messageView.setText("No articles, Pussy");
        progressBar.setVisibility(View.GONE);
        articleAdapter.clear();

        if (data != null && !data.isEmpty()) {
            Collections.sort(data);
            articleAdapter.addAll(data);
        } else {
            backgroundImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        articleAdapter.clear();
    }

}
