package io.romo.popularmovies.movielist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.romo.popularmovies.R;
import io.romo.popularmovies.util.ActivityUtils;

public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        MovieListFragment movieListFragment =
                (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.movie_list_container);
        if (movieListFragment == null) {
            movieListFragment = new MovieListFragment();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), movieListFragment, R.id.movie_list_container);
        }
    }
}
