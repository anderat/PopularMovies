package com.example.android.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utility {

  private static final String LOG_TAG = Utility.class.getSimpleName();

  private static final String API_KEY = "6abeb5d7cae3b5a7d9b2a9d91ef03a33";
  public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342/";
  public static final String BASE_URL = "https://api.themoviedb.org/3";
  private static final String API_KEY_PARAM = "api_key";
  private static final String DISCOVER_MOVIE_URL = BASE_URL + "/movie";

  public static ArrayList<Movie> fetchMovieList(String sortBy) {
    // Will contain the raw JSON response as a string.
    String resultJsonStr = null;

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    if (sortBy == null || sortBy.isEmpty()) {
      sortBy = "popular";
    }
    String fullUrl = DISCOVER_MOVIE_URL + "/" + sortBy + "?";
    try {
      Uri builtUri = Uri.parse(fullUrl).buildUpon()
              .appendQueryParameter(API_KEY_PARAM, API_KEY)
              .build();

      URL url = new URL(builtUri.toString());
      Log.v(LOG_TAG, "FBuilt URI: " + builtUri.toString());

      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      // Read the input stream into a String
      InputStream inputStream = urlConnection.getInputStream();
      StringBuffer buffer = new StringBuffer();
      if (inputStream == null) {
        return null;
      }
      reader = new BufferedReader(new InputStreamReader(inputStream));

      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line + "\n");
      }
      if (buffer.length() == 0) {
        return null;
      }
      Log.v(LOG_TAG, "Result: " + buffer.toString());
      resultJsonStr = buffer.toString();

    } catch (MalformedURLException e) {
      Log.e(LOG_TAG, "Error ", e);
      return null;
    } catch (IOException e) {
      Log.e(LOG_TAG, "Error ", e);
      return null;
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (final IOException e) {
          Log.e(LOG_TAG, "Error closing stream", e);
        }
      }
    }
    try {
      return getMovieDataFromJson(resultJsonStr);
    } catch (JSONException e) {
      Log.e(LOG_TAG, e.getMessage(), e);
    }
    return null;
  }

  public static ArrayList<Movie> getMovieDataFromJson(String jsonStr) throws JSONException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<Movie> movieList = null;

    JSONObject jsonObject = new JSONObject(jsonStr);
    Integer page = jsonObject.getInt("page");
    JSONArray resultsArray = jsonObject.getJSONArray("results");
    if (resultsArray != null) {
      movieList = new ArrayList<Movie>();
      for (int i = 0; i < resultsArray.length(); i++) {
        Movie movie = new Movie();
        JSONObject resultsItem = resultsArray.getJSONObject(i);
        Boolean adult = resultsItem.getBoolean("adult");
        movie.setAdult(adult);
        String poster_path = resultsItem.getString("poster_path");
        movie.setPosterPath(poster_path);
        String overview = resultsItem.getString("overview");
        movie.setOverview(overview);
        String release_date = resultsItem.getString("release_date");
        if (release_date != null && !release_date.isEmpty()) {
          Date releaseDate = null;
          try {
            releaseDate = sdf.parse(release_date);
          } catch (Exception e) {
            Log.e(LOG_TAG, "Release date parse", e);
          }
          movie.setReleaseDate(releaseDate);
        }
        Long id = resultsItem.getLong("id");
        movie.setId(id);
        String original_title = resultsItem.getString("original_title");
        movie.setOriginalTitle(original_title);
        String original_language = resultsItem.getString("original_language");
        movie.setOriginalLanguage(original_language);
        String title = resultsItem.getString("title");
        movie.setTitle(title);
        String backdrop_path = resultsItem.getString("backdrop_path");
        movie.setBackdropPath(backdrop_path);
        Double popularity = resultsItem.getDouble("popularity");
        movie.setPopularity(popularity);
        Integer vote_count = resultsItem.getInt("vote_count");
        movie.setVoteCount(vote_count);
        Boolean video = resultsItem.getBoolean("video");
        movie.setVideo(video);
        Double vote_average = resultsItem.getDouble("vote_average");
        movie.setVoteAverage(vote_average);

        movieList.add(movie);
      }
    }
    return movieList;
  }

}
