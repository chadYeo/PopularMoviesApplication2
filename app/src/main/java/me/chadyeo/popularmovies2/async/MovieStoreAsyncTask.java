package me.chadyeo.popularmovies2.async;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;

import me.chadyeo.popularmovies2.api.MovieResponse;
import me.chadyeo.popularmovies2.provider.MovieProvider;

public class MovieStoreAsyncTask extends AsyncTask<ArrayList<MovieResponse.Movie>, Void, Void> {

    private final Context mContext;

    public MovieStoreAsyncTask(Context context) {mContext = context;}

    @SafeVarargs
    @Override
    final protected Void doInBackground(ArrayList<MovieResponse.Movie>... params) {
        if (mContext != null) {
            ArrayList<MovieResponse.Movie> movieList = params[0];
            Uri contentUri = MovieProvider.MovieContract.CONTENT_URI;
            ContentResolver cr = mContext.getContentResolver();
            ArrayList<ContentValues> updatedValues = new ArrayList<>();
            for (MovieResponse.Movie item : movieList) {
                ContentValues value = new ContentValues();
                value.put(MovieProvider.MovieContract._ID, item.id);
                value.put(MovieProvider.MovieContract.TITLE, item.title);
                value.put(MovieProvider.MovieContract.BACKDROP_PATH, item.backdropPath);
                value.put(MovieProvider.MovieContract.POSTER_PATH, item.posterPath);
                value.put(MovieProvider.MovieContract.OVERVIEW, item.overview);
                value.put(MovieProvider.MovieContract.RATING, item.rating);
                value.put(MovieProvider.MovieContract.RELEASE_DATE, item.releaseDate);
                Uri uri = cr.insert(contentUri, value);
                if (uri.compareTo(Uri.withAppendedPath(contentUri, Long.toString(item.id))) < 0) {
                    updatedValues.add(value);
                }
            }
            for (ContentValues value : updatedValues) {
                cr.update(contentUri, value, MovieProvider.MovieContract._ID + "=?",
                        new String[] {value.getAsString(MovieProvider.MovieContract._ID)});
            }
        }
        return null;
    }
}
