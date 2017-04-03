package com.example.android.newsapp;

/**
 * Created by cking10 on 2/17/2017.
 */

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

/**
 * Created by cking10 on 2/10/2017.
 */

class Article implements Parcelable {
    private String mSection;
    private String mTitle;
    private String mPublicationDate;
    private URL mImageURL;
    private Uri mProductUri;

    Article(String mSection, String mTitle, String mPublicationDate, URL mImageURL, Uri mProductUri) {
        this.mSection = mSection;
        this.mTitle = mTitle;
        this.mPublicationDate = mPublicationDate;
        this.mImageURL = mImageURL;
        this.mProductUri = mProductUri;
    }

    protected Article(Parcel in) {
        mSection = in.readString();
        mTitle = in.readString();
        mPublicationDate = in.readString();
        mProductUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    String getmSection() {
        return mSection;
    }

    Uri getmProductUri() {
        return mProductUri;
    }

    String getmTitle() {
        return mTitle;
    }

    String getmPublicationDate() {
        return mPublicationDate;
    }


    URL getmImageURL() {
        return mImageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSection);
        dest.writeString(mTitle);
        dest.writeString(mPublicationDate);
        dest.writeParcelable(mProductUri, flags);
    }
}
