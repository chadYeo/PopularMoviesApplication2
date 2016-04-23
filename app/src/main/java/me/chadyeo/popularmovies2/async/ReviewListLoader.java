package me.chadyeo.popularmovies2.async;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

import me.chadyeo.popularmovies2.api.ReviewsResponse;
import me.chadyeo.popularmovies2.provider.MovieProvider;

public class ReviewListLoader extends AsyncTaskLoader<ArrayList<ReviewsResponse.Review>> {

    private final long mMovieId;

    private ArrayList<ReviewsResponse.Review> mReviews;

    public ReviewListLoader(Context context, long mMovieId) {
        super(context);
        this.mMovieId = mMovieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mReviews != null) {
            deliverResult(mReviews);
        } else {
            forceLoad();
        }
    }

    @Override
    public ArrayList<ReviewsResponse.Review> loadInBackground() {
        String selection = MovieProvider.COL_MOVIE_ID + "=?";
        String[] selectionArgs = new String[]{ Long.toString(mMovieId)};
        Cursor cursor = getContext().getContentResolver()
                .query(MovieProvider.ReviewContract.CONTENT_URI, null, selection, selectionArgs, "");
        if (null == cursor) {
            return null;
        } else if (cursor.getCount() < 1){
            cursor.close();
            return null;
        } else {
            mReviews = new ArrayList<>();
            int author = cursor.getColumnIndex(MovieProvider.ReviewContract.AUTHOR);
            int content = cursor.getColumnIndex(MovieProvider.ReviewContract.CONTENT);
            while (cursor.moveToNext()) {
                ReviewsResponse.Review review = new ReviewsResponse.Review();
                review.author = cursor.getString(author);
                review.content = cursor.getString(content);
                mReviews.add(review);
            }
            cursor.close();
            return mReviews;
        }
    }

    @Override
    protected void onStopLoading() { cancelLoad(); }
}
