//adapted from this guide for a more complex custom viewholder
//https://guides.codepath.com/android/using-the-recyclerview
package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

/**
 * Created by cking10 on 2/17/2017.
 */

class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> mArticles;
    private Context mContext;

    ArticleAdapter(Context context, List<Article> articles){
        mContext = context;
        mArticles = articles;
    }

    private Context getmContext() {
        return mContext;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mItemHolder;
        ImageView mImageView;
        TextView mSectionView;
        TextView mTitleView;
        TextView mPubDateView;
        View mItemDivider;

        ViewHolder(View itemView) {
            super(itemView);
            mItemHolder = (LinearLayout) itemView.findViewById(R.id.item);
            mImageView = (ImageView) itemView.findViewById(R.id.article_img);
            mSectionView = (TextView) itemView.findViewById(R.id.article_section);
            mTitleView = (TextView) itemView.findViewById(R.id.article_title);
            mPubDateView = (TextView) itemView.findViewById(R.id.article_pub_date);
            mItemDivider = itemView.findViewById(R.id.item_divider);
        }

    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View ArticleView = inflater.inflate(R.layout.list_item, parent, false);

        return new ViewHolder(ArticleView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Article currentArticle = mArticles.get(position);

        holder.mImageView.setImageBitmap(getImageBitmap(currentArticle.getmImageURL()));
        holder.mSectionView.setText(currentArticle.getmSection());
        holder.mTitleView.setText(currentArticle.getmTitle());

        //Adapted from
        //http://stackoverflow.com/questions/14853389/how-to-convert-utc-timestamp-to-device-local-time-in-android
        //Amitabha Biswas and madhu sudhan
        String formattedPubDate = null;
        try {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse(currentArticle.getmPublicationDate());

            //format date and time to locale
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault());
            formattedPubDate = df.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mPubDateView.setText(formattedPubDate);

        //Ali Kazi
        //http://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview
        //Sets last item divider to invisible
        if(position == getItemCount() - 1) { holder.mItemDivider.setVisibility(View.INVISIBLE); }

        //Click for item to open Article webpage
        holder.mItemHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri productUri = currentArticle.getmProductUri();
                if(productUri == null) {
                    Toast.makeText(getmContext(), "No link available", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, productUri);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mArticles != null)
            return mArticles.size();
        else
            return 0;
    }

    //Adapted from http://stackoverflow.com/questions/8992964/android-load-from-url-to-bitmap
    // --silentnuke
    private Bitmap getImageBitmap(URL url) {
        try {
            return new ImageFileTask().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


}

