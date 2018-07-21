package com.example.android.guardiantechnews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<LatestNews> {

    public NewsAdapter(Context context, List<LatestNews> latestNews) {
        super(context, 0, latestNews);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }
        LatestNews currentLatestNews = getItem(position);

        //get thumbnail image from url
        if (currentLatestNews.getThumbnail() != null) {
            new DownloadImageTask((ImageView) listViewItem.findViewById(R.id.thumbnail_image))
                    .execute(currentLatestNews.getThumbnail());
        } else {
            ImageView nothumnbnail = (ImageView) listViewItem.findViewById(R.id.thumbnail_image);
            nothumnbnail.setImageResource(R.drawable.nothumbnail);
        }

        //get summary
        TextView summary = (TextView) listViewItem.findViewById(R.id.summary_text);
        String summary_string = currentLatestNews.getLocation().toString();
        summary.setText(summary_string);

        // Find the TextView with view ID date
        TextView dateView = (TextView) listViewItem.findViewById(R.id.date_text);
        dateView.setText(currentLatestNews.getTime());

        //get the author of the article
        TextView authorView = (TextView) listViewItem.findViewById(R.id.author_text);
        authorView.setText(currentLatestNews.getAuthor());

        //get the section of the article
        TextView sectionView = (TextView) listViewItem.findViewById(R.id.section_text);
        sectionView.setText(currentLatestNews.getSection());

        return listViewItem;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
