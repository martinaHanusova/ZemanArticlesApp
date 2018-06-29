package com.example.android.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public static final String LOG_TAG = ArticleAdapter.class.getSimpleName();
    public ArticleAdapter(@NonNull Context context, @NonNull List<Article> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article currentArticle = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article, parent, false);
        }

        ImageView articleImage = convertView.findViewById(R.id.article_image);
        if (currentArticle.getImageUrl() != null) {
            new DownloadImageTask(articleImage).execute(currentArticle.getImageUrl());
        }

        TextView articleTitle = convertView.findViewById(R.id.article_title);
        articleTitle.setText(currentArticle.getTitle());

        TextView articleDescription = convertView.findViewById(R.id.article_description);
        articleDescription.setText(currentArticle.getDescription());

        TextView articleDate = convertView.findViewById(R.id.article_date);

        articleDate.setText(formatDate(fromISO8601UTC(currentArticle.getPublishedDate())));

        return convertView;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM yyyy");
        return dateFormat.format(date);
    }
}
