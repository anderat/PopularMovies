package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

  private MovieAdapter movieAdapter;

  private ArrayList<Movie> movieList;

  public MainActivityFragment() {
  }

  private void updateMovieList() {
    FetchMovieTask fetchMovieTask = new FetchMovieTask();
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    String sortBy = prefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
    fetchMovieTask.execute(sortBy);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
      updateMovieList();
    } else {
      movieList = savedInstanceState.getParcelableArrayList("movies");
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_main_fragment, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_sortby) {
      SortByDialogFragment newFragment = new SortByDialogFragment();
      newFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          updateMovieList();
        }
      });
      newFragment.show(getFragmentManager(), "sortbydialog");
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putParcelableArrayList("movieList", movieList);
    super.onSaveInstanceState(outState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_main, container, false);

    movieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
    GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
    gridView.setAdapter(movieAdapter);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Movie movie = movieAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class)
                .putExtra("movie", (Parcelable) movie);
        startActivity(intent);
      }
    });

    return rootView;
  }

  @Override
  public void onResume() {
    updateMovieList();
    super.onResume();
  }

  public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

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
    protected void onPostExecute(ArrayList<Movie> result) {
      if (result != null) {
        movieAdapter.clear();
        for (Movie movie : result) {
          movieAdapter.add(movie);
        }
      } else {
        Toast toast = Toast.makeText(getActivity(), R.string.msg_server_not_available, Toast.LENGTH_LONG);
        toast.show();
      }
    }

  }
}
