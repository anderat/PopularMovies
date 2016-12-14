package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import java.util.List;

public class MainActivityFragment extends Fragment implements AsyncTaskDelegate {

  private MovieAdapter movieAdapter;
  private ArrayList<Movie> movieList;
  private GridView gridView;

  public MainActivityFragment() {
  }

  private void updateMovieList() {
    Context context = getActivity();
    if (NetworkUtil.isNetworkConnected(context)) {
      FetchMovieTask fetchMovieTask = new FetchMovieTask(context, this);
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
      String sortBy = prefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
      fetchMovieTask.execute(sortBy);
    } else {
      View view = getActivity().findViewById(R.id.activity_main);
      if (view != null) {
        Snackbar snackbar = Snackbar.make(view, getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            updateMovieList();
          }
        });
        snackbar.show();
      }
    }
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

    if (movieList == null) {
      movieList = new ArrayList<Movie>();
    }
    movieAdapter = new MovieAdapter(getActivity(), movieList);
    gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
    gridView.setAdapter(movieAdapter);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Movie movie = movieAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class)
                .putExtra(Movie.PARCELABLE_KEY, (Parcelable) movie);
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

  @Override
  public void processFinish(Object output) {
    if(output != null){
      ArrayList<Movie> result = (ArrayList<Movie>)output;
      movieAdapter.clear();
      for (Movie movie : result) {
        movieAdapter.add(movie);
      }
      List<Movie> movies = (List<Movie>) output;
      if (gridView == null) {
        gridView = (GridView) getActivity().findViewById(R.id.gridview_movies);
      }
      gridView.setAdapter(new MovieAdapter(getActivity(), movies));
    } else {
      Toast toast = Toast.makeText(getActivity(), R.string.msg_server_not_available, Toast.LENGTH_LONG);
      toast.show();
    }
  }
}
