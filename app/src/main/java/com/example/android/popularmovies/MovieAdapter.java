package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {

  private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

  public MovieAdapter(Context context, List<Movie> movies) {
    super(context, 0, movies);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Movie movie = getItem(position);
    View rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movies, parent, false);

    ImageView posterImageView = (ImageView) rootView.findViewById(R.id.list_item_movies_poster);
    Picasso.with(getContext()).load(Utility.IMAGE_BASE_URL + movie.getPosterPath()).into(posterImageView);

    return rootView;
  }
}
