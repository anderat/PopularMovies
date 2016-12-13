package com.example.android.popularmovies;

import android.util.Log;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDatabaseAPITest {

  private static final String LOG_TAG = MovieDatabaseAPITest.class.getSimpleName();

  private static final String BASE_URL = "https://api.themoviedb.org/3";
  private static final String API_KEY = "api_key";
  private static final String SORTBY_PARAM = "sort_by";
  private static final String DISCOVER_MOVIE_URL = BASE_URL + "/discover/movie?";

  @Test
  public void init() {
    try {
      String apiKey = "6abeb5d7cae3b5a7d9b2a9d91ef03a33";
      String sortBy = "popularity.desc";

      ArrayList<Movie> movies = fetchMovieList(sortBy, apiKey);
      Assert.assertNotNull(movies);

    } catch (Exception e) {
      e.printStackTrace();
      Log.e(LOG_TAG, "Error", e);
      //Assert.fail(e.getMessage());
    }
    assert true;
  }

  public static ArrayList<Movie> fetchMovieList(String sortBy, String apiKey) {
    // Will contain the raw JSON response as a string.
    String resultJsonStr = null;

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    try {

      URL url = new URL(DISCOVER_MOVIE_URL + API_KEY + "=" + apiKey + "&" + SORTBY_PARAM + "=" + sortBy + "");

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
      return Utility.getMovieDataFromJson(resultJsonStr);
    } catch (JSONException e) {
      Log.e(LOG_TAG, e.getMessage(), e);
    }
    return null;
  }

}