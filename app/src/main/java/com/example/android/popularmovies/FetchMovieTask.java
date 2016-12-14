package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

  private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

  private AsyncTaskDelegate delegate = null;

  public FetchMovieTask(Context context, AsyncTaskDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  protected ArrayList<Movie> doInBackground(String... params) {
    String sortBy = params[0];
    if (sortBy == null || sortBy.isEmpty()) {
      sortBy = "popularity.desc";
    }
    ArrayList<Movie> movieList = Utility.fetchMovieList(sortBy);
    return movieList;
  }

  @Override
  protected void onPostExecute(ArrayList<Movie> movieList) {
    super.onPostExecute(movieList);
    if (delegate != null) {
      delegate.processFinish(movieList);
    }
  }

}
