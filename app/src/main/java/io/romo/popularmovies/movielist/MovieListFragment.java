package io.romo.popularmovies.movielist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.romo.popularmovies.R;
import io.romo.popularmovies.model.Movie;

public class MovieListFragment extends Fragment
        implements MovieAdapter.ListItemClickListener {

    private RecyclerView movies;
    private MovieAdapter movieAdapter;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);

        movies = v.findViewById(R.id.movies);

        movies.setHasFixedSize(true);

        GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.num_columns));
        movies.setLayoutManager(layoutManager);

        errorMessage = v.findViewById(R.id.error_message);

        loadingIndicator = v.findViewById(R.id.loading_indicator);

        // TODO Load movies

        return v;
    }

    @Override
    public void onListItemClick(Movie movie) {
        // TODO Display movie detail screen
    }

    private void showMovies() {
        movies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        movies.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
