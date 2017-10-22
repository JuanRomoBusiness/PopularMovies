package io.romo.popularmovies.movielist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.model.Movie;
import io.romo.popularmovies.model.SortBy;
import io.romo.popularmovies.moviedetails.MovieDetailsActivity;
import io.romo.popularmovies.util.JsonUtils;
import io.romo.popularmovies.util.NetworkUtils;

public class MovieListFragment extends Fragment
        implements MovieAdapter.ListItemClickListener {

    private static final String STATE_SORT_BY = "sort_by";

    @BindView(R.id.movies) RecyclerView movies;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_message) View errorMessage;
    private MovieAdapter adapter;

    // By default movies are sorted by most popular
    private SortBy sortBy = SortBy.MOST_POPULAR;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);

        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.num_columns));
        movies.setLayoutManager(layoutManager);

        movies.setHasFixedSize(true);

        adapter = new MovieAdapter(this);
        movies.setAdapter(adapter);

        if (savedInstanceState != null) {
            sortBy = (SortBy) savedInstanceState.getSerializable(STATE_SORT_BY);
        }

        loadMovies();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_SORT_BY, sortBy);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_movie_list, menu);

        SubMenu sortBySubMenu = menu.findItem(R.id.action_sort_by).getSubMenu();
        if (sortBy == SortBy.MOST_POPULAR) {
            sortBySubMenu.findItem(R.id.most_popular).setChecked(true);
        } else {
            sortBySubMenu.findItem(R.id.highest_rated).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (item.getGroupId() == R.id.sort_by) {
            SortBy previousSortBy = sortBy;

            if (itemId == R.id.most_popular) {
                sortBy = SortBy.MOST_POPULAR;
            } else {
                sortBy = SortBy.HIGHEST_RATED;
            }

            // Sorting order has not changed, therefore their is no need to reload movies
            if (sortBy == previousSortBy) {
                return true;
            }
            item.setChecked(true);

            loadMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie movie) {
        Intent intent = MovieDetailsActivity.newIntent(getActivity(), movie);
        startActivity(intent);
    }

    private void loadMovies() {
        URL url = NetworkUtils.buildUrl(sortBy);
        new LoadMoviesTask().execute(url);
    }

    private void showMovies() {
        movies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        movies.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private class LoadMoviesTask extends AsyncTask<URL, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(URL... urls) {
            URL url = urls[0];

            List<Movie> movieList = null;

            try {
                String response = NetworkUtils.getResponseFromHttpUrl(url);
                movieList = JsonUtils.getMovies(response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            progressBar.setVisibility(View.INVISIBLE);
            if (movieList != null) {
                showMovies();
                adapter.setMovieList(movieList);
                movies.getLayoutManager().scrollToPosition(0);
            } else {
                showErrorMessage();
            }
        }
    }
}
