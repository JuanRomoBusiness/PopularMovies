package io.romo.popularmovies.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.romo.popularmovies.R;
import io.romo.popularmovies.model.Movie;
import io.romo.popularmovies.util.ActivityUtils;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "io.romo.popularmovies.movie";

    public static Intent newIntent(Context packageContext, Movie movie) {
        Intent intent = new Intent(packageContext, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        MovieDetailsFragment movieDetailsFragment =
                (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.movie_details_container);
        if (movieDetailsFragment == null) {
            movieDetailsFragment = MovieDetailsFragment.newInstance(movie);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), movieDetailsFragment, R.id.movie_details_container);
        }
    }
}
