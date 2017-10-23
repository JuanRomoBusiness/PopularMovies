package io.romo.popularmovies.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.romo.popularmovies.R;
import io.romo.popularmovies.model.Movie;
import io.romo.popularmovies.util.ActivityUtils;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "io.romo.popularmovies.movie";

    @BindView(R.id.toolbar) Toolbar toolbar;

    public static Intent newIntent(Context packageContext, Movie movie) {
        Intent intent = new Intent(packageContext, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
