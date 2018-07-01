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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
        String imageUrl = currentArticle.getImageUrl();

        if (!imageUrl.equals("null")) {
            Picasso.get().load(imageUrl).transform(new CropSquareTransformation()).into(articleImage);
        } else {
            articleImage.setVisibility(View.GONE);
        }

        TextView articleTitle = convertView.findViewById(R.id.article_title);
        articleTitle.setText(currentArticle.getTitle());

        TextView articleDescription = convertView.findViewById(R.id.article_description);
        articleDescription.setText(currentArticle.getDescription());

        TextView articleDate = convertView.findViewById(R.id.article_date);

        articleDate.setText(currentArticle.getFormatedDate());

        return convertView;

    }

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        ArticleAdapter adapter;
        public DownloadImageTask(ArticleAdapter adapter, ImageView bmImage) {
            this.bmImage = bmImage;
            this.adapter = adapter;
        }

        protected Bitmap doInBackground(String... urls) {
            Log.i(LOG_TAG, "Start downloading");
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
            //adapter.notifyDataSetChanged();
            Log.i(LOG_TAG, "Setting image");
        }
    }
}
