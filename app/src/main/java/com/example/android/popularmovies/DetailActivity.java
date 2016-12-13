package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

  private Movie movie;

  private Toolbar toolbar;
  private ImageView detailPoster;
  private TextView detailReleaseDate;
  private TextView detailVoteAverage;
  private TextView detailOverview;
  private TextView detailOriginalTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    Intent intent = getIntent();
    if (intent != null && intent.hasExtra("movie") && intent.getParcelableExtra("movie") instanceof Movie) {
      movie = (Movie)intent.getParcelableExtra("movie");
    }
    if (movie != null) {
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbar.setTitle(movie.getTitle());
      detailPoster = (ImageView) findViewById(R.id.detail_poster);
      Picasso.with(getApplicationContext()).load(Utility.IMAGE_BASE_URL + movie.getPosterPath()).into(detailPoster);
      detailReleaseDate = (TextView) findViewById(R.id.detail_release_date);
      Date releaseDate = movie.getReleaseDate();
      if (releaseDate != null) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        String releaseDateStr = dateFormat.format(releaseDate);
        detailReleaseDate.setText(releaseDateStr);
      }
      detailVoteAverage = (TextView) findViewById(R.id.detail_vote_average);
      Double voteAverage = movie.getVoteAverage();
      if (voteAverage != null) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance();
        String voteAverageStr = numberFormatter.format(voteAverage);
        detailVoteAverage.setText(voteAverageStr);
      }
      detailOverview = (TextView) findViewById(R.id.detail_overview);
      String overview = movie.getOverview();
      if (overview != null) {
        detailOverview.setText(overview);
      }
      detailOriginalTitle = (TextView) findViewById(R.id.detail_original_title);
      String originalTitle = movie.getOriginalTitle();
      if (originalTitle != null) {
        detailOriginalTitle.setText(originalTitle);
      }
    }
  }

}
