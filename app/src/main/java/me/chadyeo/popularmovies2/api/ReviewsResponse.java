package me.chadyeo.popularmovies2.api;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ReviewsResponse {

    public ArrayList<Review> results;

    public static class Review implements Parcelable {

        public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }
        };

        public String author;

        public String content;

        public Review() {
        }

        protected Review(Parcel in) {
            author = in.readString();
            content = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(author);
            out.writeString(content);
        }
    }
}